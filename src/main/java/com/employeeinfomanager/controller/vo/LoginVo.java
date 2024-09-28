package com.employeeinfomanager.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginVo {
    @NotNull
    @Size(min = 4, max = 16)
    private String username;
    @NotNull
    @Size(min = 6, max = 16)
    private String password;
    @NotNull
    @Size(min = 8, max = 8)
    private String salt;
}
