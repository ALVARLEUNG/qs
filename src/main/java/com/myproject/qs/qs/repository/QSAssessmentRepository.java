package com.myproject.qs.qs.repository;

import com.myproject.qs.qs.model.QSAssessment;
import com.myproject.qs.qs.repository.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface QSAssessmentRepository extends BaseRepository<QSAssessment, String> {
    @Query(value = "select * from qs_assessment where status = 'Save' or status= 'Confirm'", nativeQuery = true)
    List<QSAssessment> findAllAssessments();

    @Query(value = "select * from qs_assessment where status= 'Confirm' and :date between open_date and close_date ", nativeQuery = true)
    List<QSAssessment> findAllActiveAssessment(@Param("date") Date date);

    @Query(value = "select * from qs_assessment where assessment_name = :assessmentName and (status = 'Save' or status= 'Confirm')", nativeQuery = true)
    QSAssessment findByAssessmentName(@Param("assessmentName") String assessmentName);
}
