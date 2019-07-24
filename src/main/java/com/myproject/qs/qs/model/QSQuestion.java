package com.myproject.qs.qs.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "qs_question")
public class QSQuestion {

    @Id
    @GeneratedValue(generator="base-uuid")
    @GenericGenerator(name="base-uuid", strategy="uuid")
    @Column(name = "ID",nullable = false)
    private String id;

    @Column(name = "section_id",updatable = false, insertable = false)
    private String sectionId;


    @Column(name = "question")
    private String question;

    @Column(name = "sequence")
    private Integer sequence;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "ID")
    private List<QSOption> optionList;

    public QSQuestion() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public List<QSOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<QSOption> optionList) {
        this.optionList = optionList;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
