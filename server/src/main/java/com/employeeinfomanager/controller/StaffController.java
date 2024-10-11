package com.employeeinfomanager.controller;

import com.employeeinfomanager.aop.Audit;
import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.common.ReturnObject;
import com.employeeinfomanager.controller.vo.CreateStaffVo;
import com.employeeinfomanager.controller.vo.UpdateStaffVo;
import com.employeeinfomanager.service.StaffService;
import com.employeeinfomanager.service.dto.StaffDto;
import com.employeeinfomanager.service.dto.StaffViolationsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/staff", produces = "application/json;charset=UTF-8")
public class StaffController {
    private Logger logger = LoggerFactory.getLogger(StaffController.class);

    private static final String NAME_REGEX = "\\S{2,6}";
    private static final String PERSON_ID_REGEX = "\\d{18}";

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PutMapping("")
    @Audit(AuditLevel.ADMIN)
    public ReturnObject createStaff(@RequestBody CreateStaffVo vo) {
        if (!vo.getName().matches(NAME_REGEX))
            return new ReturnObject(ReturnNo.FIELD_INVALID, String.format(ReturnNo.FIELD_INVALID.getMessage(), "姓名"));
        if (!vo.getPersonId().matches(PERSON_ID_REGEX))
            return new ReturnObject(ReturnNo.FIELD_INVALID, String.format(ReturnNo.FIELD_INVALID.getMessage(), "身份证号"));
        if (vo.getExperience().length() > 128)
            return new ReturnObject(ReturnNo.FIELD_INVALID, "工作经历字段过长");
        if (vo.getPhysicalCondition().length() > 128)
            return new ReturnObject(ReturnNo.FIELD_INVALID, "身体状况字段过长");
        if (vo.getAppendix().length() > 128)
            return new ReturnObject(ReturnNo.FIELD_INVALID, "其他字段过长");
        this.staffService.createStaff(vo);
        return new ReturnObject(ReturnNo.CREATED);
    }

    @DeleteMapping("")
    @Audit(AuditLevel.ADMIN)
    public ReturnObject deleteStaff(@RequestParam Long id) {
        this.staffService.deleteStaff(id);
        return new ReturnObject();
    }

    @GetMapping("")
    @Audit(AuditLevel.ADMIN)
    public ReturnObject getStaff(@RequestParam String personId) {
        StaffDto staff = this.staffService.findByPersonId(personId);
        return new ReturnObject(ReturnNo.OK, staff);
    }

    @GetMapping("/update")
    @Audit(AuditLevel.ADMIN)
    public ReturnObject updateStaff(@RequestBody UpdateStaffVo vo) {    // ? 是否需要记录创建人和修改人？
        if (!vo.getName().matches(NAME_REGEX))
            return new ReturnObject(ReturnNo.FIELD_INVALID, String.format(ReturnNo.FIELD_INVALID.getMessage(), "姓名"));
        if (!vo.getPersonId().matches(PERSON_ID_REGEX))
            return new ReturnObject(ReturnNo.FIELD_INVALID, String.format(ReturnNo.FIELD_INVALID.getMessage(), "身份证号"));
        if (vo.getExperience().length() > 128)
            return new ReturnObject(ReturnNo.FIELD_INVALID, "工作经历字段过长");
        if (vo.getPhysicalCondition().length() > 128)
            return new ReturnObject(ReturnNo.FIELD_INVALID, "身体状况字段过长");
        if (vo.getAppendix().length() > 128)
            return new ReturnObject(ReturnNo.FIELD_INVALID, "其他字段过长");
        this.staffService.updateStaff(vo);
        return new ReturnObject();
    }

    @GetMapping("/violations")
    @Audit(AuditLevel.ADMIN)
    public ReturnObject getStaffViolationsByPersonId(@RequestParam String personId) {
        StaffViolationsDto violationsDto = this.staffService.findAllViolationsByPersonId(personId);
        return new ReturnObject(ReturnNo.OK, violationsDto);
    }
}
