package com.myproject.qs.qs.repository;

import com.myproject.qs.qs.model.QSUserCategory;
import com.myproject.qs.qs.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QSUserCategoryRepository extends BaseRepository<QSUserCategory, String> {
    @Query(value = "select distinct category from qs_user_category", nativeQuery = true)
    List<String> findAllCategories();

    @Query(value = "select distinct * from qs_user_category", nativeQuery = true)
    List<QSUserCategory> findCategories();


    QSUserCategory findByCategory(String category);
}
