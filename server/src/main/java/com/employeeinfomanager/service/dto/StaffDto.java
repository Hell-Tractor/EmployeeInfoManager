package com.employeeinfomanager.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude
public class StaffDto {
    private Long id;
    private String name;
    private String image;
    private int age;
    private String personId;
    private String experience;
    private String physicalCondition;
    private String appendix;
}
