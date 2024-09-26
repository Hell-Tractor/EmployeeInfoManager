package com.employeeinfomanager.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdatePasswordVo {
    @NotNull
    @Size(min = 6, max = 16)
    private String oldPassword;
    @NotNull
    @Size(min = 6, max = 16)
    private String newPassword;
}
