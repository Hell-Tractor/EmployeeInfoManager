package com.employeeinfomanager.aop;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class UserToken {
    private Long userId;
    private String username;
    private Date expireDate;
    private AuditLevel userLevel;
    private Long departId;
}
