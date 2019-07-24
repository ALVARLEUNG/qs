package com.myproject.qs.qs.model;

import com.myproject.qs.qs.model.common.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "qs_assessment")
public class QSAssessment extends BaseEntity {

    @Column(name = "assessment_name")
    private String assessmentName;

    @Column(name = "type")
    private String type;

    @Column(name = "open_date")
    private Date openDate;

    @Column(name = "close_date")
    private Date closeDate;

    @Column(name = "status")
    private String status;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", referencedColumnName = "ID")
    private List<QSSection> sectionList;

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<QSSection> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<QSSection> sectionList) {
        this.sectionList = sectionList;
    }
}
