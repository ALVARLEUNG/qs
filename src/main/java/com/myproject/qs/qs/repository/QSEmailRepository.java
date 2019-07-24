package com.myproject.qs.qs.repository;

import com.myproject.qs.qs.domainobject.EmailObjectDo;
import com.myproject.qs.qs.model.QSEmail;
import com.myproject.qs.qs.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QSEmailRepository extends BaseRepository<QSEmail, String> {

   QSEmail findByType(String type);
}
