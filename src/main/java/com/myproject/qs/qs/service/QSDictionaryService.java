package com.myproject.qs.qs.service;

import com.myproject.qs.qs.common.exception.CustomException;
import com.myproject.qs.qs.common.response.ResultEnum;
import com.myproject.qs.qs.common.utils.DateUtil;
import com.myproject.qs.qs.model.QSDictionary;
import com.myproject.qs.qs.repository.QSDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class QSDictionaryService {

    @Autowired
    QSDictionaryRepository qsDictionaryRepository;

    @Autowired
    DateUtil dateUtil;

    public List<QSDictionary> findDictionary() {
        List<QSDictionary> qsDictionaries = qsDictionaryRepository.findAll();
        for (QSDictionary qsDictionary : qsDictionaries) {
            qsDictionary.setCreatedDate(dateUtil.dataFormat(qsDictionary.getCreatedDate()));
            qsDictionary.setModifiedDate(dateUtil.dataFormat(qsDictionary.getModifiedDate()));
        }
        return qsDictionaries;
    }

    public List<QSDictionary> updateDictionary(QSDictionary qsDictionary) {
        if (qsDictionary != null) {
            if(StringUtils.isEmpty(qsDictionary.getId())) {
                QSDictionary qsDictionaryExit = qsDictionaryRepository.findByKeyWordAndCategory(qsDictionary.getKeyWord(), qsDictionary.getCategory());
                if (qsDictionaryExit!=null) {
                    throw new CustomException(ResultEnum.DictionaryDuplicatedException.getMessage(), ResultEnum.DictionaryDuplicatedException.getCode());
                } else {
                    qsDictionaryRepository.save(qsDictionary);
                }
            } else {
                qsDictionaryRepository.save(qsDictionary);
            }
        }
        return findDictionary();
    }
}
