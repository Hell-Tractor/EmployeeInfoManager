package com.employeeinfomanager.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude
public class RiskTagDto {
    private Long id;
    private String name;
}
