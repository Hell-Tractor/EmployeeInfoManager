package com.employeeinfomanager.common;

import lombok.Getter;

@Getter
public enum ReturnNo {
    OK(200, 200, "成功"),
    CREATED(201, 201, "创建成功"),

    FIELD_INVALID(400, 400, "%s字段不合法"),
    IMAGE_SIZE_EXCEED(400, 401, "图片大小超限(最大：%s)"),
    PARAMETER_MISSED(400, 402, "缺少必要参数 %s"),
    AUTH_INVALID_ACCOUNT(400, 403, "用户名不存在或密码错误"),

    AUTH_INVALID_TOKEN(401, 410, "token不合法"),
    AUTH_TOKEN_EXPIRED(401, 411, "token过期"),
    AUTH_LOGIN_REQUIRED(401, 412, "需要先登录"),

    AUTH_NO_RIGHT(403, 430, "权限不足"),

    RESOURCE_NOT_EXIST(404, 440, "%s(id=%d)不存在"),

    INTERNAL_SERVER_ERROR(500, 500, "内部服务器错误"),

    /*---------------------ERROR in USER MODULE---------------------*/
    USER_EXIST(400, 600, "用户名为%s的用户已存在"),
    USER_NOT_EXIST(404, 601, "用户名为%s的用户不存在"),
    OLD_PASSWORD_INCORRECT(403, 602, "旧密码错误"),
    BAD_SALT(400, 603, "登录参数错误"),
    /*--------------------------------------------------------------*/
    /*--------------------ERROR in DEPART MODULE--------------------*/
    DEPART_EXIST(400, 700, "名称为%s的公司已存在"),
    DEPART_NOT_EXIST(404, 701, "名称为%s的公司不存在"),
    DEPART_STILL_IN_USE(403, 702, "公司(id=%d)仍被使用"),
    /*--------------------------------------------------------------*/
    /*-------------------ERROR in RISK TAG MODULE-------------------*/
    RISK_TAG_EXIST(400, 800, "名称为%s的风险标签已存在"),
    RISK_TAG_NOT_EXIST(404, 801, "名称为%s的风险标签不存在"),
    RISK_TAG_STILL_IN_USE(403, 802, "风险标签(id=%d)仍被使用"),
    RISK_TAG_ALREADY_IN_EMPLOYMENT(400, 803, "风险标签'%s'已经被添加至雇佣(id=%d)"),
    RISK_TAG_NOT_FOUND_IN_EMPLOYMENT(404, 804, "雇佣(id=%d)未被添加风险标签'%s'"),
    /*--------------------------------------------------------------*/
    /*---------------------ERROR in STAFF MODULE--------------------*/
    STAFF_EXIST(400, 900, "身份证号为%s的员工已存在"),
    STAFF_NOT_EXIST(404, 901, "身份证号为%s的员工不存在"),
    STAFF_STILL_IN_USE(403, 902, "员工(id=%d)仍被使用"),
    /*--------------------------------------------------------------*/
    /*---------------------ERROR in IMAGE MODULE--------------------*/
    IMAGE_EMPTY(400, 1000, "图片为空"),
    INVALID_IMAGE_TYPE(400, 1001, "未知的图片种类'%s'"),
    FAILED_READING_IMAGE(500, 1002, "图片读取失败"),
    FAILED_WRITING_IMAGE(500, 1003, "图片保存失败"),
    IMAGE_NOT_FOUND(404, 1004, "图片不存在");
    /*--------------------------------------------------------------*/

    private final int code;
    private final int httpStatusCode;
    private final String message;

    ReturnNo(int httpStatusCode, int code, String message) {
        this.httpStatusCode = httpStatusCode;
        this.code = code;
        this.message = message;
    }
}
