package com.myproject.qs.qs.vo;

import com.myproject.qs.qs.model.QSUserCategory;

import java.util.List;

public class QSAnswerUserResultVo {

    List<QSAnswerUserVo> qsAnswerUserVos;
    List<QSUserCategory> qsUserCategories;

    public List<QSAnswerUserVo> getQsAnswerUserVos() {
        return qsAnswerUserVos;
    }

    public void setQsAnswerUserVos(List<QSAnswerUserVo> qsAnswerUserVos) {
        this.qsAnswerUserVos = qsAnswerUserVos;
    }

    public List<QSUserCategory> getQsUserCategories() {
        return qsUserCategories;
    }

    public void setQsUserCategories(List<QSUserCategory> qsUserCategories) {
        this.qsUserCategories = qsUserCategories;
    }
}
