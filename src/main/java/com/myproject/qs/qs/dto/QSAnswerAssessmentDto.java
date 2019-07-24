package com.myproject.qs.qs.dto;

import com.myproject.qs.qs.model.QSAnswerDetailUser;

import java.util.List;

public class QSAnswerAssessmentDto {

    private String assessmentStatus;
    private List<QSAnswerDetailUser> answerDetailList;

    public String getAssessmentStatus() {
        return assessmentStatus;
    }

    public void setAssessmentStatus(String assessmentStatus) {
        this.assessmentStatus = assessmentStatus;
    }

    public List<QSAnswerDetailUser> getAnswerDetailList() {
        return answerDetailList;
    }

    public void setAnswerDetailList(List<QSAnswerDetailUser> answerDetailList) {
        this.answerDetailList = answerDetailList;
    }
}
