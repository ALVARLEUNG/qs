package com.myproject.qs.qs.dto;

import com.myproject.qs.qs.model.QSAnswerUser;

public class QSAnswerUserDto {
    private String assessmentName;
    private String id;
    private String category;
    private String name;
    private String email;
    private String organization;
    private String fromScore;
    private String toScore;
    private String status;
    private String keyCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getFromScore() {
        return fromScore;
    }

    public void setFromScore(String fromScore) {
        this.fromScore = fromScore;
    }

    public String getToScore() {
        return toScore;
    }

    public void setToScore(String toScore) {
        this.toScore = toScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }
}
