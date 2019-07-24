package com.myproject.qs.qs.vo;

import com.myproject.qs.qs.model.QSUser;
import com.myproject.qs.qs.model.QSUserCategory;

import java.util.List;

public class QSUserVo {

    private List<QSUser> qsUsers;
    private List<QSUserCategory> categories;

    public List<QSUser> getQsUsers() {
        return qsUsers;
    }

    public void setQsUsers(List<QSUser> qsUsers) {
        this.qsUsers = qsUsers;
    }

    public List<QSUserCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<QSUserCategory> categories) {
        this.categories = categories;
    }
}
