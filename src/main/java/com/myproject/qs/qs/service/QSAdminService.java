package com.myproject.qs.qs.service;

import com.myproject.qs.qs.common.exception.CustomException;
import com.myproject.qs.qs.common.response.ResultEnum;
import com.myproject.qs.qs.common.utils.DateUtil;
import com.myproject.qs.qs.model.QSAdmin;
import com.myproject.qs.qs.repository.QSAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class QSAdminService {

    @Autowired
    QSAdminRepository qsAdminRepository;

    @Autowired
    DateUtil dateUtil;

    public List<QSAdmin> findUserInfo() {
        List<QSAdmin> qsAdmins = qsAdminRepository.findAll();
        for (QSAdmin qsAdmin : qsAdmins) {
            qsAdmin.setCreatedDate(dateUtil.dataFormat(qsAdmin.getCreatedDate()));
            qsAdmin.setModifiedDate(dateUtil.dataFormat(qsAdmin.getModifiedDate()));
        }
        return qsAdmins;
    }

    public List<QSAdmin> updateUserInfo(QSAdmin qsAdmin) {
        if (qsAdmin != null) {
            if(StringUtils.isEmpty(qsAdmin.getId())) {
                QSAdmin qsAdminExit = qsAdminRepository.findByUserName(qsAdmin.getUserName());
                if (qsAdminExit!=null) {
                    throw new CustomException(ResultEnum.AdminUserDuplicatedException.getMessage(), ResultEnum.AdminUserDuplicatedException.getCode());
                } else {
                    qsAdminRepository.save(qsAdmin);
                }
            } else {
                qsAdminRepository.save(qsAdmin);
            }
        }
        return findUserInfo();
    }

    @Transactional
    public List<QSAdmin> deleteUsers(List<QSAdmin> esgAdminList) {
        try {
            qsAdminRepository.deleteAll(esgAdminList);
        } catch (Exception e) {
            throw new CustomException(ResultEnum.DBDeleteException.getMessage(), ResultEnum.DBDeleteException.getCode());
        }
        return findUserInfo();
    }
}
