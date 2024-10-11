package com.employeeinfomanager.service.dto;

import com.employeeinfomanager.aop.AuditLevel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
public class UserDto {
    Long id;
    String username;
    AuditLevel level;
    String departName;
}
