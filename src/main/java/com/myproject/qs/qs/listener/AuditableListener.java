package com.myproject.qs.qs.listener;

import com.myproject.qs.qs.common.utils.SessionInfoUtil;
import com.myproject.qs.qs.domainobject.Auditor;
import com.myproject.qs.qs.model.common.Auditable;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

/*
* 自定义实体类监听器
* 被@Prepersist注解的方法 ，完成save之前的操作。
* 被@Preupdate注解的方法 ，完成update之前的操作。
* 被@Postpersist注解的方法 ，完成save之后的操作。
* 被@Postupdate注解的方法 ，完成update之后的操作。
* */

@Component
public class AuditableListener {

    // 完成save之前的操作
    @PrePersist
    public void preCreate (Auditable auditable) {
        Auditor auditor = SessionInfoUtil.getLocal();
        if (null != auditor) {
            auditable.setCreatedBy(auditor.getCreatedBy());
            auditable.setModifiedBy(auditor.getModifiedBy());
        }
        Date date = new Date();
        auditable.setCreatedDate(date);
        auditable.setModifiedDate(date);
    }

    // 完成update之前的操作
    @PreUpdate
    public void preUpdate (Auditable auditable) {

        Auditor auditor = SessionInfoUtil.getLocal();

        if (null != auditor) {
            auditable.setModifiedBy(auditor.getModifiedBy());
        }
        auditable.setModifiedDate(new Date());
    }

}
