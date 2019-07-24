package com.myproject.qs.qs.common.response;

import com.myproject.qs.qs.common.constant.HttpCode;

public enum ResultEnum {

    UnknownException(HttpCode.ERROR, "Service exception, please try again or contact application support."),
    AssessmentInvalidException("02001", "Excel 文件的 [%s] 尚未完成，请检查清楚后再上传。"),
    AssessmentRowCountException("02001", "文件的列数出现不满足要求， 请检查清楚后再上传。"),
    CategoryDuplicatedException("02001", "被调查用户类型%s重复,请检查清楚后再更新。"),
    DBDeleteCorrelationException("02001","以下类型[%s]删除失败，存在此类型的用户。" ),
    UserDuplicatedException("02001","该用户的邮箱与组织已存在！" ),
    UserUploadIncomplete("02001", "[%s] 尚未完整，请补充完整再上传！"),
    CategoryNotFoundException("02001", "[%s] 不存在，请先添加以上类型再上传！"),
    AdminUserDuplicatedException("02001", "该用户已存在！"),
    DBDeleteException("02001","删除失败！" ),
    EmailTemplateDuplicatedException("02001", "该类型模板已存在！"),
    DictionaryDuplicatedException("02001", "该常量已经存在！"),
    AssessmentDuplicatedException("02001", "该问卷已经存在，请重新上传！");


    private String code;
    private String message;
    ResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
