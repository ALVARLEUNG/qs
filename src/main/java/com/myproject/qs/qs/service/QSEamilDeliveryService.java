package com.myproject.qs.qs.service;

import com.myproject.qs.qs.common.constant.Constant;
import com.myproject.qs.qs.common.exception.CustomException;
import com.myproject.qs.qs.common.response.ResultEnum;
import com.myproject.qs.qs.common.utils.DateUtil;
import com.myproject.qs.qs.common.utils.EmailGeneratorUtil;
import com.myproject.qs.qs.common.utils.UuidGeneratorUtil;
import com.myproject.qs.qs.domainobject.EmailObjectDo;
import com.myproject.qs.qs.model.QSAnswerUser;
import com.myproject.qs.qs.model.QSAssessment;
import com.myproject.qs.qs.model.QSEmail;
import com.myproject.qs.qs.model.QSUser;
import com.myproject.qs.qs.repository.QSAnswerUserRepository;
import com.myproject.qs.qs.repository.QSAssessmentRepository;
import com.myproject.qs.qs.repository.QSEmailRepository;
import com.myproject.qs.qs.repository.QSUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class QSEamilDeliveryService {

    @Autowired
    QSAssessmentRepository qsAssessmentRepository;

    @Autowired
    QSUserRepository qsUserRepository;

    @Autowired
    QSAnswerUserRepository qsAnswerUserRepository;

    @Autowired
    QSEmailRepository qsEmailRepository;

    @Autowired
    UuidGeneratorUtil uuidGeneratorUtil;

    @Autowired
    EmailGeneratorUtil emailGeneratorUtil;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    DateUtil dateUtil;

    public void inviteAllUser () {
        List<QSAssessment> qsAssessments = qsAssessmentRepository.findAllActiveAssessment(new Date());
        if(!qsAssessments.isEmpty()) {
            for (QSAssessment qsAssessment : qsAssessments) {
                invitationEmailDelivery(qsAssessment);
            }
        }
    }

    public String invitationEmailDelivery(QSAssessment qsAssessment) {

        //count数组记录邮件发送的过程，count[0]为成功发送的数量，count[1]为发送失败的数量
        int [] count = new int[2];
        List<QSUser> qsUsers = qsUserRepository.findInvitationUser(qsAssessment.getId(), qsAssessment.getType());
        if (qsUsers.isEmpty()) {
            return Constant.EMAIL_INVITE_NO_INVITATION;
        }

        for (QSUser qsUser : qsUsers) {
            count = sendInvitationEmail(qsAssessment, qsUser, count);
        }

        return String.format(Constant.EMAIL_LOG_INFO, qsUsers.size(), count[0], count[1]);
    }

    public String reminderEmailDelivery (List<String> qsAnswerUserIds) {

        int[]count = new int[2];
        if (qsAnswerUserIds.isEmpty()) {
            return remindAllUsers();
        } else {
            List<QSAnswerUser> qsAnswerUsers = new ArrayList<>();
            for (int i = 0; i<qsAnswerUserIds.size(); i++) {
                QSAnswerUser qsAnswerUser = qsAnswerUserRepository.findById(qsAnswerUserIds.get(i)).orElse(null);
                if (!dateUtil.belongCalendar(qsAnswerUser.getQsAssessment().getOpenDate(), qsAnswerUser.getQsAssessment().getCloseDate())){
                    continue;
                }
                qsAnswerUsers.add(qsAnswerUser);
            }
            if (qsAnswerUsers.isEmpty()){
                return Constant.EMAIL_REMIND_NO_REMINDER;
            }

            count = generateAndSendReminderEmail(count, qsAnswerUsers);

            return String.format(Constant.EMAIL_LOG_INFO,qsAnswerUsers.size(), count[0], count[1]);
        }
    }

    private String remindAllUsers() {
        int[] count = new int[2];
        List<QSAnswerUser> qsAnswerUsers = qsAnswerUserRepository.findByStatusAndAssessment(new Date());
        if (qsAnswerUsers.isEmpty()) {
            return Constant.EMAIL_REMIND_NO_REMINDER;
        }
        count = generateAndSendReminderEmail(count, qsAnswerUsers);
        return String.format(Constant.EMAIL_LOG_INFO,qsAnswerUsers.size(), count[0], count[1]);
    }

    private int[] generateAndSendReminderEmail(int[] count, List<QSAnswerUser> qsAnswerUsers) {
        QSEmail qsEmail = qsEmailRepository.findByType(Constant.EMAIL_TEMPLATE_TYPE_REMINDER);

        for (QSAnswerUser qsAnswerUser: qsAnswerUsers) {
            EmailObjectDo emailObjectDo = new EmailObjectDo();
            BeanUtils.copyProperties(qsAnswerUser.getQsUser(), emailObjectDo);
            BeanUtils.copyProperties(qsAnswerUser.getQsAssessment(), emailObjectDo);
            BeanUtils.copyProperties(qsEmail, emailObjectDo);
            emailObjectDo.setAnswerUserId(qsAnswerUser.getId());
            emailObjectDo.setKeyCode(qsAnswerUser.getKeyCode());
            emailObjectDo = emailGeneratorUtil.generateEmail(emailObjectDo);
            count = sendReminderEmail(emailObjectDo, count);
        }
        return count;
    }

    private int[] sendReminderEmail(EmailObjectDo emailObjectDo, int[] count) {
        int[]sum = count;
        try {
            if(emailGeneratorUtil.sendEmail(emailObjectDo)) {
                sum[0]++;
            }
        } catch (Exception e) {
            sum[1]++;
            e.printStackTrace();
        }
        return sum;
    }

    private int[] sendInvitationEmail(QSAssessment qsAssessment, QSUser qsUser, int[] count) {
        TransactionStatus status = getTransactionStatus(platformTransactionManager);
        int []sum = count;

        try {
            EmailObjectDo emailObjectDo = insertAnswerUser(qsAssessment, qsUser);
            if(emailGeneratorUtil.sendEmail(emailObjectDo)) {
                sum[0]++;
            }
            platformTransactionManager.commit(status);

        } catch (Exception e) {
            rollbackTransaction(status, platformTransactionManager);
            sum[1]++;
        }
        return sum;
    }

    private void rollbackTransaction(TransactionStatus status, PlatformTransactionManager platformTransactionManager) {
        if (!status.isCompleted()) {
            platformTransactionManager.rollback(status);
        }
    }

    private TransactionStatus getTransactionStatus(PlatformTransactionManager platformTransactionManager) {
        //开启事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return platformTransactionManager.getTransaction(def);
    }

    private EmailObjectDo insertAnswerUser(QSAssessment qsAssessment, QSUser qsUser) {

        QSEmail qsEmail = qsEmailRepository.findByType(Constant.EMAIL_TEMPLATE_TYPE_INVITATION);

        EmailObjectDo emailObjectDo = new EmailObjectDo();
        QSAnswerUser qsAnswerUser = new QSAnswerUser();
        qsAnswerUser.setQsUser(qsUser);
        qsAnswerUser.setQsAssessment(qsAssessment);
        qsAnswerUser.setStatus(Constant.ANSWER_UAER_STATUS_SENT);
        qsAnswerUser.setKeyCode(uuidGeneratorUtil.uuid());
        BeanUtils.copyProperties(qsEmail, emailObjectDo);
        BeanUtils.copyProperties(qsAssessment, emailObjectDo);
        emailObjectDo.setName(qsUser.getName());
        emailObjectDo.setEmail(qsUser.getEmail());
        emailObjectDo.setKeyCode(qsAnswerUser.getKeyCode());
        qsAnswerUser = qsAnswerUserRepository.save(qsAnswerUser);
        emailObjectDo.setAnswerUserId(qsAnswerUser.getId());
        emailObjectDo = emailGeneratorUtil.generateEmail(emailObjectDo);

        return  emailObjectDo;
    }


    public List<QSEmail> findEmailTemplate() {
        List<QSEmail> qsEmails = qsEmailRepository.findAll();
        for (QSEmail qsEmail : qsEmails) {
            qsEmail.setCreatedDate(dateUtil.dataFormat(qsEmail.getCreatedDate()));
            qsEmail.setModifiedDate(dateUtil.dataFormat(qsEmail.getModifiedDate()));
        }
        return qsEmails;
    }

    public List<QSEmail> updateEmailTemplate(QSEmail qsEmail) {
        if (qsEmail != null) {
            if(StringUtils.isEmpty(qsEmail.getId())) {
                QSEmail qsEmailExit = qsEmailRepository.findByType(qsEmail.getType());
                if (qsEmailExit!=null) {
                    throw new CustomException(ResultEnum.EmailTemplateDuplicatedException.getMessage(), ResultEnum.EmailTemplateDuplicatedException.getCode());
                } else {
                    qsEmailRepository.save(qsEmail);
                }
            } else {
                qsEmailRepository.save(qsEmail);
            }
        }
        return findEmailTemplate();
    }
}
