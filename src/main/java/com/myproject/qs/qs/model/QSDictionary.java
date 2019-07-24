package com.myproject.qs.qs.model;

import com.myproject.qs.qs.model.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "qs_dictionary")
public class QSDictionary extends BaseEntity {

    @Column(name = "category")
    private String category;

    @Column(name = "key_word")
    private String keyWord;

    @Column(name = "value")
    private String value;

    @Column(name = "sequence")
    private String sequence;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }


}
