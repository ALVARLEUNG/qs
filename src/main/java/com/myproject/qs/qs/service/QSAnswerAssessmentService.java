package com.myproject.qs.qs.service;

import com.myproject.qs.qs.common.utils.DateUtil;
import com.myproject.qs.qs.common.utils.PdfUtil;
import com.myproject.qs.qs.domainobject.QuestionDo;
import com.myproject.qs.qs.domainobject.ResultDetailDo;
import com.myproject.qs.qs.domainobject.SectionDo;
import com.myproject.qs.qs.dto.QSAnswerAssessmentDto;
import com.myproject.qs.qs.dto.QSAnswerUserDto;
import com.myproject.qs.qs.model.*;
import com.myproject.qs.qs.repository.QSAnswerDetailUserRepository;
import com.myproject.qs.qs.repository.QSAnswerUserRepository;
import com.myproject.qs.qs.repository.QSAssessmentRepository;
import com.myproject.qs.qs.repository.QSQuestionRepository;
import com.myproject.qs.qs.repository.common.QSOptionRepository;
import com.myproject.qs.qs.vo.QSAnswerDetailVo;
import com.myproject.qs.qs.vo.QSAnswerUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QSAnswerAssessmentService {

    @Autowired
    QSAssessmentRepository qsAssessmentRepository;

    @Autowired
    QSAnswerDetailUserRepository qsAnswerDetailUserRepository;

    @Autowired
    QSAnswerUserRepository qsAnswerUserRepository;

    @Autowired
    DateUtil dateUtil;

    @Autowired
    QSQuestionRepository qsQuestionRepository;

    @Autowired
    QSOptionRepository qsOptionRepository;

    public QSAnswerUserVo checkAnswerUser(QSAnswerUserDto qsAnswerUserDto) {
        QSAnswerUserVo qsAnswerUserVo = new QSAnswerUserVo();
        QSAnswerUser qsAnswerUser = qsAnswerUserRepository.findByIdAndKeyCode(qsAnswerUserDto.getId(), qsAnswerUserDto.getKeyCode());
        if (null != qsAnswerUser && null != qsAnswerUser.getQsAssessment()) {
            if (!dateUtil.belongCalendar(qsAnswerUser.getQsAssessment().getOpenDate(), qsAnswerUser.getQsAssessment().getCloseDate())) {
                // out of time range
                qsAnswerUserVo.setStatus("close");
            } else if("Completed".equals(qsAnswerUser.getStatus())) {
                qsAnswerUserVo.setStatus("completed");
            }else {
                qsAnswerUserVo.setId(qsAnswerUser.getId());
                qsAnswerUserVo.setAssessmentId(qsAnswerUser.getQsAssessment().getId());
                qsAnswerUserVo.setName(qsAnswerUser.getQsUser().getName());
                qsAnswerUserVo.setAssessmentName(qsAnswerUser.getQsAssessment().getAssessmentName());
                qsAnswerUserVo.setStatus(qsAnswerUser.getStatus());
                qsAnswerUserVo.setCategory(qsAnswerUser.getQsUser().getQsUserCategory().getCategory());
                qsAnswerUserVo.setOrganization(qsAnswerUser.getQsUser().getOrganization());
            }
        } else {
            // accessDenied
            qsAnswerUserVo.setStatus("accessDenied");
        }
        return qsAnswerUserVo;
    }

    public QSAssessment findAssessment(String assessmentId) {
        return qsAssessmentRepository.findById(assessmentId).orElse(null);
    }

    public List<QSAnswerDetailVo> findAnswerDetail(String qsAnswerUserId) {
        List<QSAnswerDetailVo> qsAnswerDetailVos = new ArrayList<>();
        List<QSAnswerDetailUser> qsAnswerDetailUsers = qsAnswerDetailUserRepository.findByAnswerUserId(qsAnswerUserId);
        for (QSAnswerDetailUser qsAnswerDetailUser : qsAnswerDetailUsers) {
            QSAnswerDetailVo qsAnswerDetailVo = new QSAnswerDetailVo();
            qsAnswerDetailVo.setSectionId(qsAnswerDetailUser.getQsQuestion().getSectionId());
            qsAnswerDetailVo.setQuestionId(qsAnswerDetailUser.getQuestionId());
            qsAnswerDetailVo.setOptionId(qsAnswerDetailUser.getOptionId());
            qsAnswerDetailVo.setAlerted(String.valueOf(qsAnswerDetailUser.getQsOption().getAlerted()));
            qsAnswerDetailVo.setScore(String.valueOf(qsAnswerDetailUser.getQsOption().getScore()));
            qsAnswerDetailVos.add(qsAnswerDetailVo);
        }
        return qsAnswerDetailVos;
    }

    public void saveAnswerDetail(QSAnswerAssessmentDto qsAnswerAssessmentDto) {
        if (qsAnswerAssessmentDto != null) {
            List<QSAnswerDetailUser> qsAnswerDetailUsers = qsAnswerAssessmentDto.getAnswerDetailList();
            if (qsAnswerDetailUsers != null) {
                saveUserAnswerDetail(qsAnswerAssessmentDto);
                if (qsAnswerAssessmentDto.getAssessmentStatus().equals("Completed")) {
                    computeScore(qsAnswerAssessmentDto.getAnswerDetailList().get(0).getAnswerUserId());
                }
            }
        }
    }

    private void computeScore(String answerUserId) {
        List<QSAnswerDetailUser> qsAnswerDetailUsers = qsAnswerDetailUserRepository.findByAnswerUserId(answerUserId);
        int sum = 0;
        for (QSAnswerDetailUser qsAnswerDetailUser: qsAnswerDetailUsers) {
            sum += qsAnswerDetailUser.getQsOption().getScore();
        }
        QSAnswerUser qsAnswerUser = qsAnswerUserRepository.findById(answerUserId).orElse(null);
        qsAnswerUser.setScore(String.valueOf(sum));
        qsAnswerUserRepository.save(qsAnswerUser);
    }

    public void saveUserAnswerDetail(QSAnswerAssessmentDto qsAnswerAssessmentDto) {
        List<QSAnswerDetailUser> qsAnswerDetailUsers = qsAnswerAssessmentDto.getAnswerDetailList();
        for (int i = 0; i < qsAnswerAssessmentDto.getAnswerDetailList().size(); i++) {
            // single
            List<QSAnswerDetailUser> qsAnswerDetailUserList = qsAnswerDetailUserRepository.findByAnswerUserIdAndQuestionId(qsAnswerDetailUsers.get(i).getAnswerUserId(), qsAnswerDetailUsers.get(i).getQuestionId());
            if (qsAnswerDetailUserList.isEmpty()) {
                // insert
                qsAnswerDetailUsers.get(i).setCreateDate(new Date());
            } else {
                // update
                qsAnswerDetailUsers.get(i).setId(qsAnswerDetailUserList.get(0).getId());
            }
            QSAnswerUser qsAnswerUser = qsAnswerUserRepository.findById(qsAnswerDetailUsers.get(i).getAnswerUserId()).orElse(null);
            qsAnswerUser.setStatus(qsAnswerAssessmentDto.getAssessmentStatus());
            qsAnswerDetailUsers.get(i).setQsQuestion(qsQuestionRepository.findById(qsAnswerDetailUsers.get(i).getQuestionId()).orElse(null));
            qsAnswerDetailUsers.get(i).setQsOption(qsOptionRepository.findById(qsAnswerDetailUsers.get(i).getOptionId()).orElse(null));
            qsAnswerDetailUsers.get(i).setQsAnswerUser(qsAnswerUser);
            qsAnswerDetailUsers.get(i).setModifiedDate(new Date());
            qsAnswerDetailUsers.get(i).setOptionId(qsAnswerAssessmentDto.getAnswerDetailList().get(i).getOptionId());

        }
        qsAnswerDetailUserRepository.saveAll(qsAnswerDetailUsers);
    }

    public QSUser checkUnsubscribe(QSAnswerUserDto qsAnswerUserDto) {
        QSUser qsUser = new QSUser();
        if (!StringUtils.isEmpty(qsAnswerUserDto.getId()) && !StringUtils.isEmpty(qsAnswerUserDto.getKeyCode())) {
            qsUser = qsAnswerUserRepository.findByIdAndKeyCode(qsAnswerUserDto.getId(), qsAnswerUserDto.getKeyCode()).getQsUser();
        }
        return qsUser;
    }

    public byte[] exportPdf(String answerUserId) {
        List<SectionDo> sectionDos = new ArrayList<>();
        QSAnswerUser qsAnswerUser = qsAnswerUserRepository.findById(answerUserId).orElse(null);
        List<QSAnswerDetailUser> qsAnswerDetailUsers = qsAnswerDetailUserRepository.findByAnswerUserId(answerUserId);
        ResultDetailDo resultDetailDo = new ResultDetailDo();
        resultDetailDo.setAssessmentName(qsAnswerUser.getQsAssessment().getAssessmentName());
        resultDetailDo.setScore(qsAnswerUser.getScore());
        resultDetailDo.setStatus(qsAnswerUser.getStatus());
        resultDetailDo.setCategory(qsAnswerUser.getQsAssessment().getType());
        resultDetailDo.setOrganization(qsAnswerUser.getQsUser().getOrganization());
        resultDetailDo.setName(qsAnswerUser.getQsUser().getName());
        for (QSSection qsSection : qsAnswerUser.getQsAssessment().getSectionList()) {
            List<QuestionDo> questionDos = new ArrayList<>();
            SectionDo sectionDo = new SectionDo();
            sectionDo.setSection(qsSection.getSection());

            for (QSQuestion qsQuestion: qsSection.getQuestionList()) {
                QuestionDo questionDo = new QuestionDo();
                questionDo.setQuestion(qsQuestion.getQuestion());
                List<String> options = new ArrayList<>();
                for (QSOption qsOption : qsQuestion.getOptionList()) {
                    String option = qsOption.getContent();
                    options.add(option);

                    for(QSAnswerDetailUser qsAnswerDetailUser: qsAnswerDetailUsers) {
                        if (qsQuestion.getId().equals(qsAnswerDetailUser.getQuestionId())) {
                            questionDo.setScore(String.valueOf(qsAnswerDetailUser.getQsOption().getScore()));
                            questionDo.setAnswerOption(qsAnswerDetailUser.getQsOption().getContent());
                            if (qsAnswerDetailUser.getQsOption().getAlerted() == 1) {
                                questionDo.setAlerted("Yes");
                            }else {
                                questionDo.setAlerted("No");
                            }
                        }
                    }
                }
                questionDo.setOptionList(options);
                questionDos.add(questionDo);

            }
            sectionDo.setQuestionDoList(questionDos);
            sectionDos.add(sectionDo);
        }

        resultDetailDo.setSectionList(sectionDos);
        return new PdfUtil().exportPdf(resultDetailDo);
    }
}
