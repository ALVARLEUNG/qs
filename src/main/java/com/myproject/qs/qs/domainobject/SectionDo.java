package com.myproject.qs.qs.domainobject;

import java.util.List;

public class SectionDo {
    private String section;
    private List<QuestionDo> questionDoList;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<QuestionDo> getQuestionDoList() {
        return questionDoList;
    }

    public void setQuestionDoList(List<QuestionDo> questionDoList) {
        this.questionDoList = questionDoList;
    }
}
