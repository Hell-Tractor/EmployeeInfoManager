package com.employeeinfomanager.controller;

import com.employeeinfomanager.aop.Audit;
import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.PageDto;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.common.ReturnObject;
import com.employeeinfomanager.controller.vo.CreateRiskTagVo;
import com.employeeinfomanager.controller.vo.UpdateRiskTagVo;
import com.employeeinfomanager.service.RiskTagService;
import com.employeeinfomanager.service.dto.RiskTagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/risk_tag", produces = "application/json;charset=UTF-8")
public class RiskTagController {
    private final RiskTagService riskTagService;

    private static final String RISK_TAG_NAME_REGEX = "\\S{1,8}";

    @Autowired
    public RiskTagController(RiskTagService riskTagService) {
        this.riskTagService = riskTagService;
    }

    @PutMapping("")
    @Audit(AuditLevel.ROOT)
    public ReturnObject createRiskTag(@RequestBody CreateRiskTagVo vo) {
        if (!vo.getName().matches(RISK_TAG_NAME_REGEX))
            return new ReturnObject(ReturnNo.FIELD_INVALID, String.format(ReturnNo.FIELD_INVALID.getMessage(), "风险标签名称"));
        this.riskTagService.createRiskTag(vo.getName());
        return new ReturnObject(ReturnNo.CREATED);
    }

    @DeleteMapping("")
    @Audit(AuditLevel.ROOT)
    public ReturnObject deleteRiskTag(@RequestParam Long id) {
        this.riskTagService.deleteRiskTag(id);
        return new ReturnObject();
    }

    @PostMapping("/update")
    @Audit(AuditLevel.ROOT)
    public ReturnObject updateRiskTag(@RequestBody UpdateRiskTagVo vo) {
        if (!vo.getName().matches(RISK_TAG_NAME_REGEX))
            return new ReturnObject(ReturnNo.FIELD_INVALID, String.format(ReturnNo.FIELD_INVALID.getMessage(), "新风险标签名称"));
        this.riskTagService.updateRiskTag(vo.getId(), vo.getName());
        return new ReturnObject();
    }

    @GetMapping("")
    @Audit(AuditLevel.ROOT)
    public ReturnObject retrieveRiskTags(@RequestParam(required = false, defaultValue = "1") int page,
                                         @RequestParam(required = false, defaultValue = "10") int pageSize) {
        PageDto<RiskTagDto> ret = this.riskTagService.retrieveRiskTags(page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }
}
