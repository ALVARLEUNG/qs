package com.myproject.qs.qs.repository;

import com.myproject.qs.qs.model.QSAdmin;
import com.myproject.qs.qs.repository.common.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QSAdminRepository extends BaseRepository<QSAdmin, String> {
    QSAdmin findByUserNameAndPassword(String userName, String password);

    QSAdmin findByUserName(String userName);
}
