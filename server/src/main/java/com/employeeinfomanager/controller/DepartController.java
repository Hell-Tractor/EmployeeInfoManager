package com.employeeinfomanager.controller;

import com.employeeinfomanager.aop.Audit;
import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.PageDto;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.common.ReturnObject;
import com.employeeinfomanager.controller.vo.CreateDepartVo;
import com.employeeinfomanager.controller.vo.UpdateDepartVo;
import com.employeeinfomanager.service.DepartService;
import com.employeeinfomanager.service.dto.DepartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/depart", produces = "application/json;charset=UTF-8")
public class DepartController {
    private final DepartService departService;

    private static final String DEPART_NAME_REGEX = "\\S{2,16}";

    @Autowired
    public DepartController(DepartService departService) {
        this.departService = departService;
    }

    @PutMapping("")
    @Audit(AuditLevel.ROOT)
    public ReturnObject createDepart(@RequestBody CreateDepartVo vo) {
        if (!vo.getName().matches(DEPART_NAME_REGEX))
            return new ReturnObject(ReturnNo.FIELD_INVALID, String.format(ReturnNo.FIELD_INVALID.getMessage(), "公司名称"));
        this.departService.createDepart(vo.getName());
        return new ReturnObject(ReturnNo.CREATED);
    }

    @DeleteMapping("")
    @Audit(AuditLevel.ROOT)
    public ReturnObject deleteDepart(@RequestParam Long id) {
        this.departService.deleteDepart(id);
        return new ReturnObject();
    }

    @PostMapping("/update")
    @Audit(AuditLevel.ROOT)
    public ReturnObject updateDepart(@RequestBody UpdateDepartVo vo) {
        if (!vo.getName().matches(DEPART_NAME_REGEX))
            return new ReturnObject(ReturnNo.FIELD_INVALID, String.format(ReturnNo.FIELD_INVALID.getMessage(), "新名称"));
        this.departService.updateDepart(vo.getId(), vo.getName());
        return new ReturnObject();
    }

    @GetMapping("")
    @Audit(AuditLevel.ROOT)
    public ReturnObject retrieveDeparts(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "10") int pageSize) {
        PageDto<DepartDto> ret = this.departService.retrieveDeparts(page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }
}
