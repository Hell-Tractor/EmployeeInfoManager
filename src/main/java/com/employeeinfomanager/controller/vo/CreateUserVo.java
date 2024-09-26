package com.employeeinfomanager.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateUserVo {
    @NotNull
    @Size(min = 4, max = 16)
    private String username;
    @NotNull
    @Size(min = 6, max = 16)
    private String password;
    @NotNull
    private Long departId;
}
