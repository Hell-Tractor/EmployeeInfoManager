package com.employeeinfomanager.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude
public class StaffViolationsDto {
    public String name;
    public List<String> violations;
}
