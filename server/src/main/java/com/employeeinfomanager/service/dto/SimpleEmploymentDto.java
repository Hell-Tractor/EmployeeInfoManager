package com.employeeinfomanager.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonInclude
public class SimpleEmploymentDto {
    private Long id;
    private Long staffId;
    private String staffName;
    private String project;
    private LocalDate validSince;
    private LocalDate validUntil;
}
