package com.myproject.qs.qs.service;

import com.myproject.qs.qs.common.exception.CustomException;
import com.myproject.qs.qs.common.response.ResultEnum;
import com.myproject.qs.qs.common.utils.DateUtil;
import com.myproject.qs.qs.model.QSUser;
import com.myproject.qs.qs.model.QSUserCategory;
import com.myproject.qs.qs.repository.QSUserCategoryRepository;
import com.myproject.qs.qs.repository.QSUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class QSUserCategoryService {

    @Autowired
    QSUserCategoryRepository qsUserCategoryRepository;

    @Autowired
    QSUserRepository qsUserRepository;

    @Autowired
    DateUtil dateUtil;


    public List<QSUserCategory> findAllCategories() {
        List<QSUserCategory> qsUserCategories = qsUserCategoryRepository.findAll();
        if(!qsUserCategories.isEmpty()) {
            for (QSUserCategory qsUserCategory: qsUserCategories) {
                qsUserCategory.setCreatedDate(dateUtil.dataFormat(qsUserCategory.getCreatedDate()));
                qsUserCategory.setModifiedDate(dateUtil.dataFormat(qsUserCategory.getModifiedDate()));
            }
        }
        return qsUserCategories;
    }

    @Transactional
    public List<QSUserCategory> updateCategory(QSUserCategory qsUserCategory) {
        if (null != qsUserCategory) {
            QSUserCategory categoryExit = qsUserCategoryRepository.findByCategory(qsUserCategory.getCategory());
            if (null !=categoryExit && StringUtils.isEmpty(qsUserCategory.getId())) {
                    throw new CustomException(String.format(ResultEnum.CategoryDuplicatedException.getMessage(), qsUserCategory.getCategory()),ResultEnum.CategoryDuplicatedException.getCode());
            }
            qsUserCategoryRepository.save(qsUserCategory);
        }
        return findAllCategories();
    }

    @Transactional
    public List<QSUserCategory> removeCategory (List<QSUserCategory> qsUserCategories) {
        List <String> relativeCategory = new ArrayList<>();
        if( !qsUserCategories.isEmpty() ) {
            for (QSUserCategory qsUserCategory: qsUserCategories) {
                List<QSUser> qsUsers = qsUserRepository.findByCategoryId(qsUserCategory.getId());
                if (!qsUsers.isEmpty()) {
                    relativeCategory.add(qsUserCategory.getCategory());
                } else {
                    qsUserCategoryRepository.delete(qsUserCategory);
                }
            }

            if (relativeCategory.size()>0) {
                throw new CustomException(String.format(ResultEnum.DBDeleteCorrelationException.getMessage(), relativeCategory), ResultEnum.DBDeleteCorrelationException.getCode());
            }
        }
        return findAllCategories();
    }
}
