package com.employeeinfomanager.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateEmploymentVo {
    @NotNull
    private Long staffId;
    @NotNull
    private Long departId;
    @NotNull
    @Size(min = 1, max = 16)
    private String project;
    @NotNull
    private LocalDate validSince;
    @NotNull
    private LocalDate validUntil;
    @NotNull
    private String workPermit;
    @NotNull
    private List<Long> riskTagIds;
}
