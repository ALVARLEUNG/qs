package com.myproject.qs.qs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailConfig {
    @Value("${spring_mail_username}")
    private String emailFrom;

    @Value("${test_email_address}")
    private String testEmailAddress;

    public String getEmailFrom() { return emailFrom; }

    public String getTestEmailAddress() {
        return testEmailAddress;
    }
}
