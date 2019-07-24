package com.myproject.qs.qs.domainobject;

import java.util.List;

public class QuestionDo {
    private String question;
    private List<String> optionList;
    private String score;
    private String alerted;
    private String answerOption;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
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

    public String getAnswerOption() {
        return answerOption;
    }

    public void setAnswerOption(String answerOption) {
        this.answerOption = answerOption;
    }
}
