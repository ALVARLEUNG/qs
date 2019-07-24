package com.myproject.qs.qs.service;

import com.myproject.qs.qs.common.constant.Constant;
import com.myproject.qs.qs.common.exception.CustomException;
import com.myproject.qs.qs.common.response.ResultEnum;
import com.myproject.qs.qs.common.utils.DateUtil;
import com.myproject.qs.qs.common.utils.ExcelUtil;
import com.myproject.qs.qs.model.QSAssessment;
import com.myproject.qs.qs.model.QSOption;
import com.myproject.qs.qs.model.QSQuestion;
import com.myproject.qs.qs.model.QSSection;
import com.myproject.qs.qs.repository.QSAssessmentRepository;
import com.myproject.qs.qs.repository.QSUserCategoryRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class QSAssessmentService {

    @Autowired
    QSAssessmentRepository qsAssessmentRepository;

    @Autowired
    QSUserCategoryRepository qsUserCategoryRepository;

    @Autowired
    DateUtil dateUtil;

    @Autowired
    QSEamilDeliveryService qsEamilDeliveryService;

    private ExecutorService executor = Executors.newCachedThreadPool() ;

    public List<QSAssessment> findAllAssessments () {
        List<QSAssessment> qsAssessments = qsAssessmentRepository.findAllAssessments();
        if(!qsAssessments.isEmpty()) {
            for (QSAssessment qsAssessment: qsAssessments) {
                if (null != qsAssessment.getOpenDate() && null != qsAssessment.getCloseDate()) {
                    qsAssessment.setOpenDate(dateUtil.dataFormat(qsAssessment.getOpenDate()));
                    qsAssessment.setCloseDate(dateUtil.dataFormat(qsAssessment.getCloseDate()));
                }
                qsAssessment.setCreatedDate(dateUtil.dataFormat(qsAssessment.getCreatedDate()));
                qsAssessment.setModifiedDate(dateUtil.dataFormat(qsAssessment.getModifiedDate()));
            }
        }
        return qsAssessments;
    }

    public void deleteAssessment(QSAssessment qsAssessment) {
        qsAssessment.setStatus(Constant.ASSESSMENT_STATUS_DELETE);
        qsAssessment.setModifiedDate(new Date());
        qsAssessmentRepository.save(qsAssessment);
    }

    public void confirmAssessment(QSAssessment qsAssessment) {
        qsAssessment.setStatus(Constant.ASSESSMENT_STATUS_CONFIRM);
        qsAssessment.setModifiedDate(new Date());
        asyncSendInvitationEmail(qsAssessmentRepository.save(qsAssessment));

    }

    public QSAssessment readInfoFromExcel(MultipartFile file) {
        Workbook wb = ExcelUtil.getWorkbookFromFile(file);
        QSAssessment qsAssessment = new QSAssessment();
        if (wb != null) {
                // the first sheet of workbook
                Sheet sheet = wb.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();
                List<QSSection> sectionList = new ArrayList<>();
                Row title = rowIterator.next();
                int index = 0;
                for (int i = 0; i<title.getPhysicalNumberOfCells();i++){
                    if(title.getCell(i).getStringCellValue().equals("")) {
                        break;
                    }
                    index++;
                }
                if (index != 6) {
                    throw new CustomException(ResultEnum.AssessmentRowCountException.getMessage(), ResultEnum.AssessmentRowCountException.getCode());
                }
                if (rowIterator.hasNext()) {
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        // Assessment Name columns
                        if (ExcelUtil.validateData(row, 0)) {
                            QSAssessment qsAssessmentExit = qsAssessmentRepository.findByAssessmentName(ExcelUtil.getStringCellValue(row.getCell(0)).trim());
                            if (null != qsAssessmentExit) {
                                throw new CustomException(ResultEnum.AssessmentDuplicatedException.getMessage(), ResultEnum.AssessmentDuplicatedException.getCode());
                            }else {
                                qsAssessment.setAssessmentName(ExcelUtil.getStringCellValue(row.getCell(0)));
                            }
                        }

                        // Section columns
                        if (ExcelUtil.validateData(row, 1)) {
                                sectionList = parseSection(sectionList, row);
                        }
                        // Question columns
                        if (ExcelUtil.validateData(row, 2)) {
                                sectionList = parseQuestion(sectionList, row);
                        }

                        // Option columns
                        if (ExcelUtil.validateData(row, 3)) {
                                QSOption option = parseOption(row);

                                    if (ExcelUtil.isFull(row, 4, 5)) {
                                        option.setScore(Integer.valueOf(ExcelUtil.getStringCellValue(row.getCell(4))));
                                        option.setAlerted(Integer.valueOf(ExcelUtil.getStringCellValue(row.getCell(5))));
                                    } else {
                                        throw new CustomException(String.format(ResultEnum.AssessmentInvalidException.getMessage(), "是否敏感 & 选项分数"), ResultEnum.AssessmentInvalidException.getCode());
                                    }

                                for (QSSection qsSection : sectionList) {
                                    if (qsSection.getSequence() == sectionList.size()) {
                                        for (QSQuestion qsQuestion : qsSection.getQuestionList()) {
                                            if (qsQuestion.getSequence() == qsSection.getQuestionList().size()) {
                                                option.setSequence(qsQuestion.getOptionList().size() + 1);
                                                qsQuestion.getOptionList().add(option);
                                            }
                                        }
                                    }
                                }
                            }
                    }
                    qsAssessment.setSectionList(sectionList);
                } else {
                    throw new CustomException(String.format(ResultEnum.AssessmentInvalidException.getMessage(), "主要数据"), ResultEnum.AssessmentInvalidException.getCode());
                }
        } else {
            throw new CustomException(String.format(ResultEnum.AssessmentInvalidException.getMessage(), "SHEET"), ResultEnum.AssessmentInvalidException.getCode());
        }
        return qsAssessment;
    }

    public List<QSSection> parseSection(List<QSSection> qsSections, Row row) {
        QSSection qsSection = new QSSection();
        String section = ExcelUtil.getStringCellValue(row.getCell(1));

        qsSection.setSection(section);
        qsSection.setSequence(qsSections.size() + 1);
        qsSections.add(qsSection);

        List<QSQuestion> qsQuestions = new ArrayList<>();
        qsSection.setQuestionList(qsQuestions);
        return qsSections;
    }

    public List<QSSection> parseQuestion(List<QSSection> qsSections, Row row) {
        QSQuestion qsQuestion = new QSQuestion();
        String question = ExcelUtil.getStringCellValue(row.getCell(2));

        qsQuestion.setQuestion(question);

        List<QSOption> qsOptions = new ArrayList<>();
        qsQuestion.setOptionList(qsOptions);

        for (QSSection section : qsSections) {
            if (section.getSequence() == qsSections.size()) {
                qsQuestion.setSequence(section.getQuestionList().size() + 1);
                section.getQuestionList().add(qsQuestion);
            }
        }
        return qsSections;
    }

    public QSOption parseOption(Row row) {
        QSOption qsOption = new QSOption();
        String option = ExcelUtil.getStringCellValue(row.getCell(3));
        qsOption.setContent(option);
        return qsOption;
    }

    public List<String> findAssessmentType() {
        List<String> assessmentType =qsUserCategoryRepository.findAllCategories();
        return assessmentType;
    }

    public void saveAssessment(QSAssessment qsAssessment) {
        qsAssessment.setStatus(Constant.ASSESSMENT_STATUS_SAVE);
        qsAssessmentRepository.save(qsAssessment);
    }

    public void asyncSendInvitationEmail (QSAssessment qsAssessment) {
         executor.submit(new Runnable() {

            @Override
            public void run() {
                try{
                    qsEamilDeliveryService.invitationEmailDelivery(qsAssessment);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
