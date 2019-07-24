package com.myproject.qs.qs.common.utils;

import com.myproject.qs.qs.common.constant.Constant;
import com.myproject.qs.qs.config.EmailConfig;
import com.myproject.qs.qs.domainobject.EmailObjectDo;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;

@Component
public class EmailGeneratorUtil {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    EmailConfig emailConfig;

    public EmailObjectDo generateEmail (EmailObjectDo emailObjectDo) {
        String inviteUrl = Constant.INVITE_URL_PREFIX + "/" + emailObjectDo.getAnswerUserId() + "/" + emailObjectDo.getKeyCode();
        String unsubscribeUrl = Constant.UNSUBSCRIBE_URL_PREFIX + "/" + emailObjectDo.getAnswerUserId() + "/" + emailObjectDo.getKeyCode();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String openDateContent = format.format(emailObjectDo.getOpenDate());
        String closeDateContent = format.format(emailObjectDo.getCloseDate());

        emailObjectDo.setSubject(emailObjectDo.getSubject().replaceAll(Constant.EMAIL_TEMPLATE_TAG_ASSESSMENT, emailObjectDo.getAssessmentName()));
        emailObjectDo.setContent(emailObjectDo.getContent().replaceAll(Constant.EMAIL_TEMPLATE_TAG_ASSESSMENT, emailObjectDo.getAssessmentName())
                                .replaceAll(Constant.EMAIL_TEMPLATE_TAG_OPEN_DATE, openDateContent).replaceAll(Constant.EMAIL_TEMPLATE_TAG_CLOSE_DATE, closeDateContent)
                                .replaceAll(Constant.EMAIL_TEMPLATE_TAG_NAME, emailObjectDo.getName()).replaceAll(Constant.EMAIL_TEMPLATE_TAG_INVITATION_URL, inviteUrl)
                                .replaceAll(Constant.EMAIL_TEMPLATE_TAG_UNSUBSCRIBE_URL, unsubscribeUrl));

        return emailObjectDo;
    }


    public boolean sendEmail (EmailObjectDo emailObjectDo) throws Exception {
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

        if (StringUtils.isEmpty(emailConfig.getTestEmailAddress())) {
            message.setTo(emailObjectDo.getEmail());
        }else {
            message.setTo(emailConfig.getTestEmailAddress());
        }

        message.setFrom(emailConfig.getEmailFrom());
        message.setCc(emailConfig.getEmailFrom());
        message.setSubject(emailObjectDo.getSubject());
        message.setText(emailObjectDo.getContent(),true);
        this.javaMailSender.send(mimeMessage);
        return true;
    }

}
