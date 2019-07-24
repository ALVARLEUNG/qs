package com.myproject.qs.qs.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "qs_answer_detail_user")
public class QSAnswerDetailUser {

    @Id
    @GeneratedValue(generator="base-uuid")
    @GenericGenerator(name="base-uuid", strategy="uuid")
    @Column(name = "ID",nullable = false)
    private String id;

    @Column(name = "answer_user_id",updatable = false, insertable = false)
    private String answerUserId;

    @Column(name = "question_id",updatable = false, insertable = false)
    private String questionId;

    @Column(name = "option_id",updatable = false, insertable = false)
    private String optionId;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "answer_user_id", referencedColumnName = "ID")
    private QSAnswerUser qsAnswerUser;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", referencedColumnName = "ID")
    private QSQuestion qsQuestion;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "option_id", referencedColumnName = "ID")
    private QSOption qsOption;

    @CreatedDate
    @Column(name = "created_date",updatable = false)
    private Date createDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private Date modifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QSAnswerUser getQsAnswerUser() {
        return qsAnswerUser;
    }

    public void setQsAnswerUser(QSAnswerUser qsAnswerUser) {
        this.qsAnswerUser = qsAnswerUser;
    }

    public QSQuestion getQsQuestion() {
        return qsQuestion;
    }

    public void setQsQuestion(QSQuestion qsQuestion) {
        this.qsQuestion = qsQuestion;
    }

    public QSOption getQsOption() {
        return qsOption;
    }

    public void setQsOption(QSOption qsOption) {
        this.qsOption = qsOption;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(String answerUserId) {
        this.answerUserId = answerUserId;
    }
}
