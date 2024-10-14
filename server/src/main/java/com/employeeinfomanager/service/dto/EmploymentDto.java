package com.employeeinfomanager.service.dto;

import com.employeeinfomanager.dao.bo.RiskTag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@JsonInclude
public class EmploymentDto {
    private Long id;
    private StaffDto staff;
    private DepartDto depart;
    private String project;
    private LocalDate validSince;
    private LocalDate validUntil;
    private List<RiskTag> riskTags;
    private String workPermit;
    private String violation;
}
