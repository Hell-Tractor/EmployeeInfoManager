package com.employeeinfomanager.controller;

import com.employeeinfomanager.aop.Audit;
import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.aop.LoginUser;
import com.employeeinfomanager.common.PageDto;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.common.ReturnObject;
import com.employeeinfomanager.controller.vo.LoginVo;
import com.employeeinfomanager.controller.vo.UpdatePasswordVo;
import com.employeeinfomanager.controller.vo.CreateUserVo;
import com.employeeinfomanager.service.dto.UserDto;
import com.employeeinfomanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @Audit(AuditLevel.ROOT)
    public ReturnObject createUser(@Valid @RequestBody CreateUserVo vo) {
        this.userService.createUser(vo.getUsername(), vo.getPassword(), vo.getDepartId());
        return new ReturnObject(ReturnNo.CREATED);
    }

    @PostMapping("/login")
    public ReturnObject login(@Valid @RequestBody LoginVo vo) {
        String token = this.userService.login(vo.getUsername(), vo.getPassword());
        return new ReturnObject(ReturnNo.OK, ReturnNo.OK.getMessage(), token);
    }

    @GetMapping("")
    @Audit(AuditLevel.ROOT)
    public ReturnObject getUsers(@RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "10") int pageSize) {
        PageDto<UserDto> ret = this.userService.retrieveUsers(page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @DeleteMapping("/delete/force")
    @Audit(AuditLevel.ROOT)
    public ReturnObject deleteUser(@RequestParam Long userId) {
        this.userService.deleteUser(userId);
        return new ReturnObject(ReturnNo.OK);
    }

    @DeleteMapping("/delete")
    @Audit
    public ReturnObject deleteSelf(@LoginUser Long userId) {
        this.userService.deleteUser(userId);
        return new ReturnObject(ReturnNo.OK);
    }

    @PostMapping("/update")
    @Audit
    public ReturnObject updatePassword(@LoginUser Long userId, @Valid @RequestBody UpdatePasswordVo vo) {
        this.userService.updatePassword(userId, vo.getOldPassword(), vo.getNewPassword());
        return new ReturnObject(ReturnNo.OK);
    }
}
