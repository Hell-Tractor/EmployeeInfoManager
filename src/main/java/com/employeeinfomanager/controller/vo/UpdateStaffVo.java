package com.employeeinfomanager.controller.vo;

import lombok.Data;

@Data
public class UpdateStaffVo {
    private Long id;
    private String name;
    private String image;
    private int bornYear;
    private String personId;
    private String experience;
    private String physicalCondition;
    private String appendix;
}
