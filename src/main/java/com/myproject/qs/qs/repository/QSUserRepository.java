package com.myproject.qs.qs.repository;

import com.myproject.qs.qs.model.QSUser;
import com.myproject.qs.qs.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QSUserRepository extends BaseRepository<QSUser, String> {
    @Query(value = "select * from qs_user as qu,qs_user_category as quc where qu.category_id = quc.id and quc.category = :category " +
            "and qu.unsubscribed=0 and qu.active=1 and qu.id not in " +
            "(select user_id from qs_answer_user where assessment_id = :assessment_id )", nativeQuery = true)
    List<QSUser> findInvitationUser(@Param("assessment_id") String assessmentId, @Param("category")String assessmentType);

    @Query(value = "select * from qs_user where category_id = :categoryId", nativeQuery = true)
    List<QSUser> findByCategoryId(@Param("categoryId") String categoryId);

    @Query(value = "select * from qs_user where active = 1", nativeQuery = true)
    List<QSUser> findAllActiveUsers();

    @Query(value = "select * from qs_user  where email = :email and category_id = :categoryId and organization = :organization", nativeQuery = true)
    QSUser findByEmailAndCategoryAndOrganization(@Param("email") String email, @Param("categoryId") String categoryId, @Param("organization")String organization);
}
