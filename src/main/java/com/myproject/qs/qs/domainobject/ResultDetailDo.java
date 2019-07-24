package com.myproject.qs.qs.domainobject;
import java.util.List;

public class ResultDetailDo {

    private String assessmentName;
    private String name;
    private String category;
    private String organization;
    private String status;
    private String score;
    private List<SectionDo> sectionList;

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<SectionDo> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<SectionDo> sectionList) {
        this.sectionList = sectionList;
    }
}
