package com.myproject.qs.qs.service;

import com.myproject.qs.qs.common.utils.SessionInfoUtil;
import com.myproject.qs.qs.domainobject.Auditor;
import com.myproject.qs.qs.dto.QSLoginInfoDto;
import com.myproject.qs.qs.model.QSAdmin;
import com.myproject.qs.qs.repository.QSAdminRepository;
import com.myproject.qs.qs.vo.QSLoginInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;

@Service
public class QSLoginService {

    @Autowired
    QSAdminRepository qsAdminRepository;

    public QSLoginInfoVo checkLogin(QSLoginInfoDto qsLoginInfoDto, HttpSession session) {
        QSAdmin qsAdmin = qsAdminRepository.findByUserNameAndPassword(qsLoginInfoDto.getUserName(), qsLoginInfoDto.getPassword());
        QSLoginInfoVo qsLoginInfoVo = new QSLoginInfoVo();
        if (null != qsAdmin) {
            qsLoginInfoVo.setDisplayName(qsAdmin.getDisplayName());
            qsLoginInfoVo.setType(qsAdmin.getType());
            Auditor auditor = new Auditor();
            auditor.setId(qsAdmin.getId());
            auditor.setCreatedBy(qsAdmin.getDisplayName());
            auditor.setModifiedBy(qsAdmin.getDisplayName());
//            SessionInfoUtil.setLocal(auditor);
            session.setAttribute("userInfo", auditor);
            return  qsLoginInfoVo;
        }
        return null;
    }
}
