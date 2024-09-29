package com.employeeinfomanager.controller;

import com.employeeinfomanager.aop.Audit;
import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.aop.LoginUserToken;
import com.employeeinfomanager.aop.UserToken;
import com.employeeinfomanager.common.PageDto;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.common.ReturnObject;
import com.employeeinfomanager.controller.vo.CreateEmploymentVo;
import com.employeeinfomanager.controller.vo.UpdateEmploymentVo;
import com.employeeinfomanager.service.EmploymentService;
import com.employeeinfomanager.service.dto.EmploymentDto;
import com.employeeinfomanager.service.dto.SimpleEmploymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/employment", produces = "applications/json;charset=UTF-8")
public class EmploymentController {
    private final EmploymentService employmentService;

    @Autowired
    public EmploymentController(EmploymentService employmentService) {
        this.employmentService = employmentService;
    }

    @PutMapping("")
    @Audit(AuditLevel.ADMIN)
    public ReturnObject createEmployment(@LoginUserToken UserToken token, @RequestBody CreateEmploymentVo vo) {
        this.employmentService.createEmployment(token, vo);
        return new ReturnObject(ReturnNo.CREATED);
    }

    @GetMapping("")
    public ReturnObject getEmploymentById(@RequestParam Long id) {
        EmploymentDto ret = this.employmentService.getEmployment(id);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/update")
    @Audit(AuditLevel.ADMIN)
    public ReturnObject updateEmployment(@LoginUserToken UserToken token, @RequestBody UpdateEmploymentVo vo) {
        this.employmentService.updateEmployment(token, vo);
        return new ReturnObject(ReturnNo.OK);
    }

    @GetMapping("/depart/{departId}/all")
    @Audit(AuditLevel.ADMIN)
    public ReturnObject retrieveEmploymentsByDepartId(@LoginUserToken UserToken token, @PathVariable Long departId,
                                                      @RequestParam(required = false, defaultValue = "1") int page,
                                                      @RequestParam(required = false, defaultValue = "10") int pageSize) {
        PageDto<SimpleEmploymentDto> ret = this.employmentService.retrieveEmploymentsByDepartId(token, departId, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @PutMapping("/{employmentId}/risk_tag")
    @Audit(AuditLevel.ADMIN)
    public ReturnObject addRiskTagToEmployment(@LoginUserToken UserToken token, @PathVariable Long employmentId, @RequestParam Long riskTagId) {
        this.employmentService.addRiskTagToEmployment(token, employmentId, riskTagId);
        return new ReturnObject(ReturnNo.CREATED);
    }

    @DeleteMapping("/{employmentId}/risk_tag")
    @Audit(AuditLevel.ADMIN)
    public ReturnObject removeRiskTagFromEmployment(@LoginUserToken UserToken token, @PathVariable Long employmentId, @RequestParam Long riskTagId) {
        this.employmentService.removeRiskTagFromEmployment(token, employmentId, riskTagId);
        return new ReturnObject();
    }
}
