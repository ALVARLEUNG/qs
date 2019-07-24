package com.myproject.qs.qs.repository;

import com.myproject.qs.qs.model.QSAnswerDetailUser;
import com.myproject.qs.qs.model.QSAnswerUser;
import com.myproject.qs.qs.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface QSAnswerUserRepository extends BaseRepository<QSAnswerUser, String> {
    @Query(value = "select * from qs_answer_user as qau, qs_assessment as qa where qau.assessment_id = qa.id and " +
            "qau.status = 'Sent' or qau.status = 'In Progress'" +
            "and :date between qa.open_date and qa.close_date ", nativeQuery = true)
    List<QSAnswerUser> findByStatusAndAssessment(@Param("date")Date date);

    QSAnswerUser findByIdAndKeyCode(String id, String keyCode);
}
