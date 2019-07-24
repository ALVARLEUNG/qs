package com.myproject.qs.qs.repository;

import com.myproject.qs.qs.model.QSAnswerDetailUser;
import com.myproject.qs.qs.repository.common.BaseRepository;

import java.util.List;

public interface QSAnswerDetailUserRepository extends BaseRepository<QSAnswerDetailUser, String> {
    List<QSAnswerDetailUser> findByAnswerUserId(String answerUserId);

    List<QSAnswerDetailUser> findByAnswerUserIdAndQuestionId(String answerUserId, String questionId);
}
