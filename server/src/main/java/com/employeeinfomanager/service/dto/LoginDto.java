package com.employeeinfomanager.service.dto;

import com.employeeinfomanager.aop.AuditLevel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String token;
    private AuditLevel level;
    private Long departId;
}
