package com.myproject.qs.qs.model;

import com.myproject.qs.qs.model.common.BaseEntity;
import javax.persistence.*;


@Entity
@Table(name = "qs_user")
public class QSUser extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "ID")
    private QSUserCategory qsUserCategory;

    @Column(name = "category_id",updatable = false, insertable = false)
    private String categoryId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "organization")
    private String organization;

    @Column(name = "active")
    private String active;

    @Column(name = "unsubscribed")
    private String unsubscribed;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public QSUserCategory getQsUserCategory() {
        return qsUserCategory;
    }

    public void setQsUserCategory(QSUserCategory qsUserCategory) {
        this.qsUserCategory = qsUserCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getUnsubscribed() {
        return unsubscribed;
    }

    public void setUnsubscribed(String unsubscribed) {
        this.unsubscribed = unsubscribed;
    }

}