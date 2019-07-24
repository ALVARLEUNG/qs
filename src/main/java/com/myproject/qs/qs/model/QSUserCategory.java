package com.myproject.qs.qs.model;

import com.myproject.qs.qs.model.common.BaseEntity;
import javax.persistence.*;


@Entity
@Table(name = "qs_user_category")
public class QSUserCategory extends BaseEntity {

    @Column(name = "category")
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
