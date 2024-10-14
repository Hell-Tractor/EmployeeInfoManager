package com.employeeinfomanager.service;

import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.aop.UserToken;
import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.PageDto;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.controller.vo.CreateEmploymentVo;
import com.employeeinfomanager.controller.vo.UpdateEmploymentVo;
import com.employeeinfomanager.dao.EmploymentDao;
import com.employeeinfomanager.dao.RiskTagDao;
import com.employeeinfomanager.dao.bo.Employment;
import com.employeeinfomanager.service.dto.DepartDto;
import com.employeeinfomanager.service.dto.EmploymentDto;
import com.employeeinfomanager.service.dto.SimpleEmploymentDto;
import com.employeeinfomanager.service.dto.StaffDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmploymentService {
    private final EmploymentDao employmentDao;
    private final RiskTagDao riskTagDao;

    @Autowired
    public EmploymentService(EmploymentDao employmentDao, RiskTagDao riskTagDao) {
        this.employmentDao = employmentDao;
        this.riskTagDao = riskTagDao;
    }

    private void checkPermissions(UserToken token, Long departId) {
        if (token.getUserLevel() == AuditLevel.ROOT)
            return;
        if (token.getUserLevel() == AuditLevel.ADMIN && departId == token.getDepartId())
            return;
        throw new BusinessException(ReturnNo.AUTH_NO_RIGHT);
    }

    public static EmploymentDto getDto(Employment bo) {
        StaffDto staffDto = StaffService.getDto(bo.getStaff());
        DepartDto departDto = DepartService.getDto(bo.getDepart());
        return EmploymentDto.builder().id(bo.getId()).staff(staffDto).depart(departDto).project(bo.getProject())
                .validSince(bo.getValidSince()).validUntil(bo.getValidUntil())
                .riskTags(bo.getRiskTags()).workPermit(bo.getWorkPermit())
                .violation(bo.getViolation()).build();
    }

    public static SimpleEmploymentDto getSimpleDto(Employment bo) {
        return SimpleEmploymentDto.builder().id(bo.getId()).staffId(bo.getStaffId())
                .staffName(bo.getStaff().getName()).project(bo.getProject())
                .validSince(bo.getValidSince()).validUntil(bo.getValidUntil()).build();
    }

    @Transactional
    public void createEmployment(UserToken token, CreateEmploymentVo vo) {
        this.checkPermissions(token, vo.getDepartId());
        Employment bo = Employment.builder().id(null).staffId(vo.getStaffId()).departId(vo.getDepartId())
                .project(vo.getProject()).validSince(vo.getValidSince()).validUntil(vo.getValidUntil())
                .workPermit(vo.getWorkPermit()).violation(vo.getViolation()).build();
        this.employmentDao.insert(bo, vo.getRiskTagIds());
    }

    @Transactional
    public void deleteEmployment(UserToken token, Long id) {
        Employment employment = this.employmentDao.findById(id);
        this.checkPermissions(token, employment.getDepartId());

        this.employmentDao.deleteById(id);
    }

    @Transactional
    public EmploymentDto getEmployment(Long id) {
        Employment employment = this.employmentDao.findById(id);
        return getDto(employment);
    }

    @Transactional
    public PageDto<SimpleEmploymentDto> retrieveEmploymentsByDepartId(UserToken token, Long departId, int page, int pageSize) {
        this.checkPermissions(token, departId);
        List<SimpleEmploymentDto> ret = this.employmentDao.retrieveEmploymentsByDepartId(departId, page, pageSize).stream().map(EmploymentService::getSimpleDto).toList();
        Long count = this.employmentDao.getEmploymentCountByDepartId(departId);
        return new PageDto<>(ret, page, ret.size(), count);
    }

    @Transactional
    public void updateEmployment(UserToken token, UpdateEmploymentVo vo) {
        this.checkPermissions(token, vo.getDepartId());
        Employment bo = Employment.builder().id(vo.getId()).staffId(vo.getStaffId()).departId(vo.getDepartId())
                .project(vo.getProject()).validSince(vo.getValidSince()).validUntil(vo.getValidUntil())
                .workPermit(vo.getWorkPermit()).violation(vo.getViolation()).build();
        this.employmentDao.save(bo);
    }

    @Transactional
    public void addRiskTagToEmployment(UserToken token, Long employmentId, Long riskTagId) {
        Employment employment = this.employmentDao.findById(employmentId);
        this.checkPermissions(token, employment.getDepartId());

        this.riskTagDao.addRiskTagToEmployment(employmentId, riskTagId);
    }

    @Transactional
    public void removeRiskTagFromEmployment(UserToken token, Long employmentId, Long riskTagId) {
        Employment employment = this.employmentDao.findById(employmentId);
        this.checkPermissions(token, employment.getDepartId());

        this.riskTagDao.removeRiskTagFromEmployment(employmentId, riskTagId);
    }
}
