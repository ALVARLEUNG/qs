package com.myproject.qs.qs.vo;

public class QSAnswerDetailVo {
    private String questionId;
    private String optionId;
    private String sectionId;
    private String score;
    private String alerted;
    private String optionSequence;

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

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAlerted() {
        return alerted;
    }

    public void setAlerted(String alerted) {
        this.alerted = alerted;
    }

    public String getOptionSequence() {
        return optionSequence;
    }

    public void setOptionSequence(String optionSequence) {
        this.optionSequence = optionSequence;
    }
}
