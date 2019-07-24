package com.myproject.qs.qs.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "qs_section")
public class QSSection {

    @Id
    @GeneratedValue(generator="base-uuid")
    @GenericGenerator(name="base-uuid", strategy="uuid")
    @Column(name = "ID",nullable = false)
    private String id;

    @Column(name = "section")
    private String section;

    @Column(name = "assessment_id",updatable = false, insertable = false)
    private String assessmentId;

    @Column(name = "sequence")
    private Integer sequence;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", referencedColumnName = "ID")
    private List<QSQuestion> questionList;

    public QSSection () {}

    public void setId(String id) {
        this.id = id;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public void setQuestionList(List<QSQuestion> questionList) {
        this.questionList = questionList;
    }

    public String getId() {
        return id;
    }

    public String getSection() {
        return section;
    }

    public Integer getSequence() {
        return sequence;
    }

    public List<QSQuestion> getQuestionList() {
        return questionList;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }
}
