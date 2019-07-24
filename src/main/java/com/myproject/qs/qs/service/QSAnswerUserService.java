package com.myproject.qs.qs.service;

import com.myproject.qs.qs.dto.QSAnswerUserDto;
import com.myproject.qs.qs.model.QSAnswerUser;
import com.myproject.qs.qs.model.QSAssessment;
import com.myproject.qs.qs.model.QSUser;
import com.myproject.qs.qs.repository.QSAnswerUserRepository;
import com.myproject.qs.qs.repository.QSUserCategoryRepository;
import com.myproject.qs.qs.vo.QSAnswerUserResultVo;
import com.myproject.qs.qs.vo.QSAnswerUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class QSAnswerUserService {

    @Autowired
    QSAnswerUserRepository qsAnswerUserRepository;

    @Autowired
    QSUserCategoryRepository qsUserCategoryRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    QSEamilDeliveryService qsEamilDeliveryService;

    private ExecutorService executor = Executors.newCachedThreadPool() ;

    public QSAnswerUserResultVo findAllAnswerUser() {

        QSAnswerUserResultVo qsAnswerUserResultVo = new QSAnswerUserResultVo();
        qsAnswerUserResultVo.setQsAnswerUserVos(findAnswerUsers());
        qsAnswerUserResultVo.setQsUserCategories(qsUserCategoryRepository.findCategories());
        return qsAnswerUserResultVo;
    }

    public List<QSAnswerUserVo> findAnswerUsers(){
        List<QSAnswerUserVo> qsAnswerUserVos = new ArrayList<>();
        String sql = "select qau.id, qa.assessment_name, qau.assessment_id, quc.category, qu.name, qu.email, qu.organization, qau.score, qau.status from qs_answer_user as qau, qs_user as qu, qs_assessment as qa, qs_user_category as quc " +
                "where qau.user_id = qu.id and qau.assessment_id = qa.id and qu.category_id = quc.id";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        for (Map row : results) {
            QSAnswerUserVo qsAnswerUserVo = new QSAnswerUserVo();
            qsAnswerUserVo.setId(String.valueOf((row.get("ID"))));
            qsAnswerUserVo.setAssessmentName(String.valueOf((row.get("ASSESSMENT_NAME"))));
            qsAnswerUserVo.setAssessmentId(String.valueOf((row.get("ASSESSMENT_ID"))));
            qsAnswerUserVo.setCategory(String.valueOf((row.get("CATEGORY"))));
            qsAnswerUserVo.setName(String.valueOf((row.get("NAME"))));
            qsAnswerUserVo.setEmail(String.valueOf((row.get("EMAIL"))));
            qsAnswerUserVo.setOrganization(String.valueOf((row.get("ORGANIZATION"))));
            qsAnswerUserVo.setScore(String.valueOf((row.get("SCORE"))));
            qsAnswerUserVo.setStatus(String.valueOf((row.get("STATUS"))));
            qsAnswerUserVos.add(qsAnswerUserVo);
        }
        return qsAnswerUserVos;
    }

    public List<QSAnswerUserVo> searchAnswerUserByConditions(QSAnswerUserDto qsAnswerUserDto) {
        List<QSAnswerUser> qsAnswerUsers = qsAnswerUserRepository.findAll(new Specification<QSAnswerUser>() {
            @Override
            public Predicate toPredicate(Root<QSAnswerUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();

                if (!StringUtils.isEmpty(qsAnswerUserDto.getAssessmentName())) {
                    Join<QSAssessment, QSAnswerUser> join = root.join("qsAssessment", JoinType.LEFT);
                    predicateList.add(criteriaBuilder.like(join.get("assessmentName"), "%" + qsAnswerUserDto.getAssessmentName().trim() + "%"));
                }

                if (!StringUtils.isEmpty(qsAnswerUserDto.getName())) {
                    Join<QSUser, QSAnswerUser> join = root.join("qsUser", JoinType.LEFT);
                    predicateList.add(criteriaBuilder.like(join.get("name"), "%" + qsAnswerUserDto.getName().trim() + "%"));
                }

                if (!StringUtils.isEmpty(qsAnswerUserDto.getEmail())) {
                    Join<QSUser, QSAnswerUser> join = root.join("qsUser", JoinType.LEFT);
                    predicateList.add(criteriaBuilder.like(join.get("email"), "%" + qsAnswerUserDto.getEmail().trim() + "%"));
                }

                if (!StringUtils.isEmpty(qsAnswerUserDto.getCategory())) {
                    Join<QSUser, QSAnswerUser> join = root.join("qsUser", JoinType.LEFT);
                    predicateList.add(criteriaBuilder.equal(join.get("qsUserCategory").get("category"),  qsAnswerUserDto.getCategory()));
                }

                if (!StringUtils.isEmpty(qsAnswerUserDto.getOrganization())) {
                    Join<QSUser, QSAnswerUser> join = root.join("qsUser", JoinType.LEFT);
                    predicateList.add(criteriaBuilder.like(join.get("organization"), "%"+ qsAnswerUserDto.getOrganization().trim()+ "%"));
                }
                if (!StringUtils.isEmpty(qsAnswerUserDto.getFromScore()) && !StringUtils.isEmpty(qsAnswerUserDto.getToScore())) {
                    Path score = root.get("score");
                    Predicate p = criteriaBuilder.between(score, qsAnswerUserDto.getFromScore().trim(), qsAnswerUserDto.getToScore().trim());
                    predicateList.add(p);
                }

                if (!StringUtils.isEmpty(qsAnswerUserDto.getFromScore()) && StringUtils.isEmpty(qsAnswerUserDto.getToScore())) {
                    Path score = root.get("score");
                    Predicate p = criteriaBuilder.greaterThanOrEqualTo(score, qsAnswerUserDto.getFromScore().trim());
                    predicateList.add(p);
                }

                if (StringUtils.isEmpty(qsAnswerUserDto.getFromScore()) && !StringUtils.isEmpty(qsAnswerUserDto.getToScore())) {
                    Path score = root.get("score");
                    Predicate p = criteriaBuilder.lessThanOrEqualTo(score, qsAnswerUserDto.getToScore().trim());
                    predicateList.add(p);
                }

                if (!StringUtils.isEmpty(qsAnswerUserDto.getStatus())) {
                    Path status = root.get("status");
                    Predicate p = criteriaBuilder.equal(status, qsAnswerUserDto.getStatus());
                    predicateList.add(p);
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                criteriaQuery.where(predicates);
                return criteriaBuilder.and(predicateList.toArray(predicates));
            }
        });

        return generatorAnswerUserVos(qsAnswerUsers);
    }

    private List<QSAnswerUserVo> generatorAnswerUserVos(List<QSAnswerUser> qsAnswerUsers) {
        List<QSAnswerUserVo> qsAnswerUserVos = new ArrayList<>();
        for (QSAnswerUser qsAnswerUser: qsAnswerUsers) {
            QSAnswerUserVo qsAnswerUserVo = new QSAnswerUserVo();
            BeanUtils.copyProperties(qsAnswerUser, qsAnswerUserVo);
            qsAnswerUserVo.setAssessmentId(qsAnswerUser.getQsAssessment().getId());
            qsAnswerUserVo.setAssessmentName(qsAnswerUser.getQsAssessment().getAssessmentName());
            qsAnswerUserVo.setName(qsAnswerUser.getQsUser().getName());
            qsAnswerUserVo.setEmail(qsAnswerUser.getQsUser().getEmail());
            qsAnswerUserVo.setOrganization(qsAnswerUser.getQsUser().getOrganization());
            qsAnswerUserVo.setCategory(qsAnswerUser.getQsUser().getQsUserCategory().getCategory());
            qsAnswerUserVos.add(qsAnswerUserVo);
        }
        return qsAnswerUserVos;
    }

    public void remindUsers(List<String> qsAnswerUserIds) {
       asyncSendReminderEmail(qsAnswerUserIds);
    }

    public void asyncSendReminderEmail (List<String> qsAnswerUserIds) {
        executor.submit(new Runnable() {

            @Override
            public void run() {
                try{
                    qsEamilDeliveryService.reminderEmailDelivery(qsAnswerUserIds);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
