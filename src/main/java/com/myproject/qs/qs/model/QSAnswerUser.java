package com.myproject.qs.qs.model;

import com.myproject.qs.qs.model.common.BaseEntity;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "qs_answer_user")
public class QSAnswerUser  {

    @Id
    @GeneratedValue(generator="base-uuid")
    @GenericGenerator(name="base-uuid", strategy="uuid")
    @Column(name = "ID",nullable = false)
    private String id;

    @ManyToOne (fetch=FetchType.EAGER)
    @JoinColumn(name = "assessment_id",referencedColumnName = "id")
    private QSAssessment qsAssessment;

    @ManyToOne (fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private QSUser qsUser;

    @Column(name = "status")
    private String status;

    @Column(name = "score")
    private String score;

    @Column(name = "key_code")
    private  String keyCode;

    @Column(name = "created_date")
    private  Date createdDate;

    @Column(name = "modified_date")
    private  Date modifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QSAssessment getQsAssessment() {
        return qsAssessment;
    }

    public void setQsAssessment(QSAssessment qsAssessment) {
        this.qsAssessment = qsAssessment;
    }

    public QSUser getQsUser() {
        return qsUser;
    }

    public void setQsUser(QSUser qsUser) {
        this.qsUser = qsUser;
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

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
