package com.myproject.qs.qs.dto;

import com.myproject.qs.qs.model.QSUser;

public class QSUserDto extends QSUser {

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
