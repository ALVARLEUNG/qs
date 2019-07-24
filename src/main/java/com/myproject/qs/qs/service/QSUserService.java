package com.myproject.qs.qs.service;

import com.myproject.qs.qs.common.exception.CustomException;
import com.myproject.qs.qs.common.response.ResultEnum;
import com.myproject.qs.qs.common.utils.DateUtil;
import com.myproject.qs.qs.common.utils.ExcelUtil;
import com.myproject.qs.qs.common.utils.SessionInfoUtil;
import com.myproject.qs.qs.common.utils.UuidGeneratorUtil;
import com.myproject.qs.qs.domainobject.Auditor;
import com.myproject.qs.qs.model.QSUser;
import com.myproject.qs.qs.model.QSUserCategory;
import com.myproject.qs.qs.repository.QSUserCategoryRepository;
import com.myproject.qs.qs.repository.QSUserRepository;
import com.myproject.qs.qs.vo.QSUserVo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class QSUserService {

    @Autowired
    QSUserRepository qsUserRepository;

    @Autowired
    QSUserCategoryRepository qsUserCategoryRepository;

    @Autowired
    DateUtil dateUtil;

    @Autowired
    UuidGeneratorUtil uuidGeneratorUtil;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public QSUserVo findAllUsers() {
        QSUserVo qsUserVo = new QSUserVo();
        List<QSUser> qsUsers = qsUserRepository.findAllActiveUsers();
        List<QSUserCategory> categories = qsUserCategoryRepository.findCategories();
        for (QSUser qsUser : qsUsers) {
            qsUser.setCreatedDate(dateUtil.dataFormat(qsUser.getCreatedDate()));
            qsUser.setModifiedDate(dateUtil.dataFormat(qsUser.getModifiedDate()));
        }
        qsUserVo.setQsUsers(qsUsers);
        qsUserVo.setCategories(categories);
        return qsUserVo;
    }


    public QSUserVo updateUser(QSUser qsUser) {
        if (null != qsUser) {
            QSUser userExit = qsUserRepository.findByEmailAndCategoryAndOrganization(qsUser.getEmail(), qsUser.getCategoryId(), qsUser.getOrganization());
            if (null != userExit && StringUtils.isEmpty(qsUser.getId())) {
                throw new CustomException(ResultEnum.UserDuplicatedException.getMessage(), ResultEnum.UserDuplicatedException.getCode());
            }
            qsUser.setActive("1");
            qsUser.setQsUserCategory(qsUserCategoryRepository.findById(qsUser.getCategoryId()).orElse(null));
            qsUserRepository.save(qsUser);
        }
        return findAllUsers();
    }


    public void readInfoFromExcel(MultipartFile file) {
        Auditor auditor = SessionInfoUtil.getLocal();
        List<QSUser> existUsers = qsUserRepository.findAll();
        List<QSUserCategory> categories = qsUserCategoryRepository.findAll();
        Workbook wb = ExcelUtil.getWorkbookFromFile(file);
        if (wb != null) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            Row row = null;
            rowIterator.next();
            int rowIndex = 1;
            List<QSUser> userList = new ArrayList<>();
            List<QSUser> updateUserList = new ArrayList<>();
            List<String> notFoundCategoryList = new ArrayList<>();
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                if (ExcelUtil.isFull(row, 0, 3)) {
                    String category = ExcelUtil.getStringCellValue(row.getCell(0));
                    String name = ExcelUtil.getStringCellValue(row.getCell(1));
                    String email = ExcelUtil.getStringCellValue(row.getCell(2));
                    String organization = ExcelUtil.getStringCellValue(row.getCell(3));
                    QSUserCategory userCategory = isCategoryExist(category.trim(), categories);
                    if (userCategory != null) {
                        QSUser qsUser = new QSUser();
                        qsUser.setQsUserCategory(userCategory);
                        qsUser.setCategoryId(userCategory.getId());
                        qsUser.setName(name);
                        qsUser.setEmail(email);
                        qsUser.setOrganization(organization);
                        qsUser.setUnsubscribed("0");
                        qsUser.setActive("1");
                        qsUser.setModifiedBy(auditor.getModifiedBy());
                        QSUser qsUerExists = compareUser(qsUser, existUsers);
                        if (qsUerExists != null) {
                            qsUser.setId(String.valueOf(qsUerExists.getId()));
                            qsUser.setModifiedBy(auditor.getModifiedBy());
                            updateUserList.add(qsUser);
                        } else {
                            qsUser.setCreatedBy(auditor.getCreatedBy());
                            qsUser.setModifiedBy(auditor.getModifiedBy());
                            userList.add(qsUser);
                        }
                    } else {
                        notFoundCategoryList.add(category);
                    }
                } else {
                    throw new CustomException(String.format(ResultEnum.UserUploadIncomplete.getMessage(), rowIndex), ResultEnum.UserUploadIncomplete.getCode());
                }
                rowIndex += 1;
            }
            if (!notFoundCategoryList.isEmpty()) {
                throw new CustomException(String.format(ResultEnum.CategoryNotFoundException.getMessage(), notFoundCategoryList), ResultEnum.CategoryNotFoundException.getCode());
            }
            if (!updateUserList.isEmpty()) {
                updateUsers(updateUserList);
            }
            if (!userList.isEmpty()) {
                insertUsers(userList);
            }
        }
    }


    private QSUserCategory isCategoryExist(String category, List<QSUserCategory> categories) {
        QSUserCategory qsUserCategory = null;
        int size = categories.size();
        for (int i = 0; i < size; i++) {
            if (category.equals(categories.get(i).getCategory())) {
                qsUserCategory = categories.get(i);
                break;
            }
        }
        return qsUserCategory;
    }

    private QSUser compareUser(QSUser qsUser, List<QSUser> existUsers) {
        QSUser result = null;
        if (null != existUsers) {
            int length = existUsers.size();
            for (int i = 0; i < length; i++) {
                if (existUsers.get(i).getOrganization().equals(qsUser.getOrganization()) && existUsers.get(i).getEmail().equals(qsUser.getEmail()) &&
                        existUsers.get(i).getQsUserCategory().getCategory().equals(qsUser.getQsUserCategory().getCategory())) {
                    result = existUsers.get(i);
                    break;
                }
            }
        }
        return result;
    }

//    private void changeAllActiveValue() {
//        String sql = "update qs_user set ACTIVE = 0 where ACTIVE = 1";
//        jdbcTemplate.update(sql);
//    }

    private void insertUsers(List<QSUser> qsUsers) {
        String sql = "INSERT INTO qs_user(ID, ORGANIZATION, NAME, EMAIL, CATEGORY_ID,ACTIVE, UNSUBSCRIBED, CREATED_DATE, CREATED_BY, MODIFIED_DATE, MODIFIED_BY) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                QSUser qsUser = qsUsers.get(i);
                preparedStatement.setString(1, uuidGeneratorUtil.uuid());
                preparedStatement.setString(2, qsUser.getOrganization());
                preparedStatement.setString(3, qsUser.getName());
                preparedStatement.setString(4, qsUser.getEmail());
                preparedStatement.setString(5, qsUser.getCategoryId());
                preparedStatement.setString(6, qsUser.getActive());
                preparedStatement.setString(7, qsUser.getUnsubscribed());
                preparedStatement.setDate(8, new java.sql.Date(new Date().getTime()));
                preparedStatement.setString(9, qsUser.getCreatedBy());
                preparedStatement.setDate(10, new java.sql.Date(new Date().getTime()));
                preparedStatement.setString(11, qsUser.getModifiedBy());
            }

            @Override
            public int getBatchSize() {
                return qsUsers.size();
            }
        });
    }

    public void updateUsers(List<QSUser> qsUsers) {
        String sql = "update qs_user set NAME=?, CATEGORY_ID=?, ACTIVE=?, UNSUBSCRIBED=?, MODIFIED_DATE=?, MODIFIED_BY=? where ID=?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                QSUser qsUser = qsUsers.get(i);
                preparedStatement.setString(1, qsUser.getName());
                preparedStatement.setString(2, qsUser.getCategoryId());
                preparedStatement.setString(3, qsUser.getActive());
                preparedStatement.setString(4, qsUser.getUnsubscribed());
                preparedStatement.setDate(5, new java.sql.Date(new Date().getTime()));
                preparedStatement.setString(6, qsUser.getModifiedBy());
                preparedStatement.setString(7, qsUser.getId());
            }

            @Override
            public int getBatchSize() {
                return qsUsers.size();
            }
        });
    }

    public List<QSUser> searchUserByConditions(QSUser qsUser) {
        List<QSUser> qsUsers = qsUserRepository.findAll(new Specification<QSUser>() {
            @Override
            public Predicate toPredicate(Root<QSUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(qsUser.getOrganization())) {
                    Path organization = root.get("organization");
                    Predicate p1 = criteriaBuilder.like(criteriaBuilder.upper(organization), "%" + qsUser.getOrganization().toUpperCase() + "%");
                    predicateList.add(p1);
                }
                if (!StringUtils.isEmpty(qsUser.getName())) {
                    Path name = root.get("name");
                    Predicate p2 = criteriaBuilder.like(criteriaBuilder.upper(name), "%" + qsUser.getName().toUpperCase() + "%");
                    predicateList.add(p2);
                }
                if (!StringUtils.isEmpty(qsUser.getEmail())) {
                    Path email = root.get("email");
                    Predicate p3 = criteriaBuilder.equal(email, qsUser.getEmail());
                    predicateList.add(p3);
                }
                if (!StringUtils.isEmpty(qsUser.getCategoryId())) {
                    Path categoryId = root.get("categoryId");
                    Predicate p4 = criteriaBuilder.equal(categoryId, qsUser.getCategoryId());
                    predicateList.add(p4);
                }
                if (!StringUtils.isEmpty(qsUser.getUnsubscribed())) {
                    Path unsubscribed = root.get("unsubscribed");
                    Predicate p8 = criteriaBuilder.equal(unsubscribed, qsUser.getUnsubscribed());
                    predicateList.add(p8);
                }
                // active = 1
                Path active = root.get("active");
                Predicate p = criteriaBuilder.equal(active, 1);
                predicateList.add(p);

                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                criteriaQuery.where(predicates);
                return criteriaBuilder.and(predicates);
            }
        });
        for (QSUser userResult : qsUsers) {
            userResult.setCreatedDate(dateUtil.dataFormat(userResult.getCreatedDate()));
            userResult.setModifiedDate(dateUtil.dataFormat(userResult.getModifiedDate()));
        }
        return qsUsers;
    }

    public void userUnsubscribed(QSUser qsUser) {
        String sql = "update qs_user set unsubscribed = 1, modified_by = ? where id = ? ";
        jdbcTemplate.update(sql, qsUser.getName(), qsUser.getId());
    }

}
