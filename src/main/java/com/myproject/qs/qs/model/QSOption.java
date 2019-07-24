package com.myproject.qs.qs.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "qs_option")
public class QSOption {

    @Id
    @GeneratedValue(generator="base-uuid")
    @GenericGenerator(name="base-uuid", strategy="uuid")
    @Column(name = "ID",nullable = false)
    private String id;

    @Column(name = "question_id",updatable = false, insertable = false)
    private String questionId;

    @Column(name = "content")
    private String content;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "alerted")
    private Integer alerted;

    @Column(name = "score")
    private Integer score;

    public QSOption () {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getAlerted() {
        return alerted;
    }

    public void setAlerted(Integer alerted) {
        this.alerted = alerted;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
