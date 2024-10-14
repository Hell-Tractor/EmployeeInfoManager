package com.employeeinfomanager.controller.vo;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CreateStaffVo {
    private String name;
    private String image;
    @Size(min = 18, max = 100)
    private int age;
    private String personId;
    private String experience;
    private String physicalCondition;
    private String appendix;
}
