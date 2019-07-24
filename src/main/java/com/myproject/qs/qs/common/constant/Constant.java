package com.myproject.qs.qs.common.constant;

public class Constant {

    //URL Prefix
    public static final String INVITE_URL_PREFIX = "localhost:8091";
    public static final String UNSUBSCRIBE_URL_PREFIX = "localhost:8091/unsubscribed";

    // Email Delivery
    public static final String EMAIL_INVITE_NO_INVITATION = "There are no users to invite";
    public static final String EMAIL_REMIND_NO_REMINDER = "There are no users to remind";
    public static final String EMAIL_TEMPLATE_TAG_NAME = "<User_Name>";
    public static final String EMAIL_TEMPLATE_TAG_ASSESSMENT = "<Assessment_No>";
    public static final String EMAIL_TEMPLATE_TAG_OPEN_DATE ="<Open_Date>";
    public static final String EMAIL_TEMPLATE_TAG_CLOSE_DATE = "<Close_Date>";
    public static final String EMAIL_TEMPLATE_TAG_INVITATION_URL = "<Invitation_Url>";
    public static final String EMAIL_TEMPLATE_TAG_UNSUBSCRIBE_URL = "<Unsubscribe_Url>";
    public static final String EMAIL_LOG_INFO = "共:%s; 发送成功:%s; 发送失败:%s";
    public static final String EMAIL_TEMPLATE_TYPE_INVITATION = "Questionnaire_Email_Invitation";
    public static final String EMAIL_TEMPLATE_TYPE_REMINDER = "Questionnaire_Email_Reminder";

    // Answer User Status
    public static final String ANSWER_UAER_STATUS_SENT = "Sent";

    // Assessment Status
    public static final String ASSESSMENT_STATUS_DELETE = "Delete";
    public static final String ASSESSMENT_STATUS_SAVE = "Save";
    public static final String ASSESSMENT_STATUS_CONFIRM = "Confirm";
}
