package com.myproject.qs.qs.repository;

import com.myproject.qs.qs.model.QSDictionary;
import com.myproject.qs.qs.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QSDictionaryRepository extends BaseRepository<QSDictionary, String> {
    QSDictionary findByKeyWordAndCategory(String dictKey, String category);
}
