package com.myproject.qs.qs.model.common;


import com.myproject.qs.qs.listener.AuditableListener;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Hibernate 事件监听器.
 * 实体监听@EntityListeners(AuditListener.class).
 */
@MappedSuperclass
@EntityListeners(AuditableListener.class)
public class BaseEntity implements Serializable, Auditable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator="base-uuid")
    @GenericGenerator(name="base-uuid", strategy="uuid")
    @Column(name = "ID",nullable = false)
    private String id;

    @CreatedDate
    @Column(name = "created_date",updatable = false)
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "created_by", length = 100, updatable = false)
    private String createdBy;

    @Column(name = "modified_by", length = 100)
    private String modifiedBy;


    @Override
    public String getCreatedBy() { return this.createdBy; }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String getModifiedBy() {
        return this.modifiedBy;
    }

    @Override
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public Date getCreatedDate() {
        return this.createdDate;
    }

    @Override
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    @Override
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
