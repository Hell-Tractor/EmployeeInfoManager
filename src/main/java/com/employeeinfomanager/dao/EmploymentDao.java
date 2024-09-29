package com.employeeinfomanager.dao;

import com.employeeinfomanager.Mapper.EmploymentPoMapper;
import com.employeeinfomanager.Mapper.EmploymentRiskTagPoMapper;
import com.employeeinfomanager.Mapper.po.EmploymentPo;
import com.employeeinfomanager.Mapper.po.EmploymentRiskTagPo;
import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.bo.Employment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmploymentDao {
    private final EmploymentPoMapper employmentPoMapper;
    private final EmploymentRiskTagPoMapper employmentRiskTagPoMapper;
    private final DepartDao departDao;
    private final RiskTagDao riskTagDao;
    private final StaffDao staffDao;

    @Autowired
    public EmploymentDao(EmploymentPoMapper employmentPoMapper, EmploymentRiskTagPoMapper employmentRiskTagPoMapper, DepartDao departDao, RiskTagDao riskTagDao, StaffDao staffDao) {
        this.employmentPoMapper = employmentPoMapper;
        this.employmentRiskTagPoMapper = employmentRiskTagPoMapper;
        this.departDao = departDao;
        this.riskTagDao = riskTagDao;
        this.staffDao = staffDao;
    }

    private void setBo(Employment bo) {
        bo.setDepartDao(this.departDao);
        bo.setRiskTagDao(this.riskTagDao);
        bo.setStaffDao(this.staffDao);
    }

    private Employment getBo(EmploymentPo po) {
        Employment bo = Employment.builder().id(po.getId()).staffId(po.getStaffId()).departId(po.getDepartId())
                .project(po.getProject()).validSince(po.getValidSince()).validUntil(po.getValidUntil())
                .workPermit(po.getWorkPermit()).violation(po.getViolation()).build();
        setBo(bo);
        return bo;
    }

    private EmploymentPo getPo(Employment bo) {
        return EmploymentPo.builder().id(bo.getId()).staffId(bo.getStaffId()).departId(bo.getDepartId())
                .project(bo.getProject()).validSince(bo.getValidSince()).validUntil(bo.getValidUntil())
                .workPermit(bo.getWorkPermit()).violation(bo.getViolation()).build();
    }

    public List<Long> retrieveDistinctEmploymentIdsByRiskTagId(Long riskTagId) {
        return this.employmentRiskTagPoMapper.findDistinctEmploymentIdByRiskTagId(riskTagId);
    }

    public List<Long> retrieveEmploymentIdsByStaffId(Long staffId) {
        return this.employmentPoMapper.findAllEmploymentIdByStaffId(staffId);
    }

    public List<String> retrieveViolationsByStaffId(Long staffId) {
        return this.employmentPoMapper.findAllViolationByStaffId(staffId);
    }

    public List<Employment> retrieveEmploymentsByDepartId(Long departId, int page, int pageSize) {
        return this.employmentPoMapper.findAllByDepartId(departId, PageRequest.of(page - 1, pageSize)).stream()
                .map(this::getBo).toList();
    }

    public Employment findById(Long id) {
        Optional<EmploymentPo> po = this.employmentPoMapper.findById(id);
        if (po.isEmpty())
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "雇佣", id));
        return getBo(po.get());
    }

    public void insert(Employment employment, List<Long> riskTagIds) {
        checkBo(employment);

        EmploymentPo po = getPo(employment);
        po.setId(null);
        this.employmentPoMapper.save(po);
        employment.setId(po.getId());

        for (Long id : riskTagIds) {
            this.riskTagDao.findById(id); // check if riskTag exists
            EmploymentRiskTagPo employmentRiskTagPo = new EmploymentRiskTagPo(null, employment.getId(), id);
            this.employmentRiskTagPoMapper.save(employmentRiskTagPo);
        }
    }

    public void save(Employment employment) {
        if (null == employment.getId())
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "雇佣", -1));
        this.findById(employment.getId());
        checkBo(employment);

        this.employmentPoMapper.save(getPo(employment));
    }

    private void checkBo(Employment employment) {
        setBo(employment);
        if (null == employment.getStaff())
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "员工", employment.getStaffId()));
        if (null == employment.getDepart())
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "公司", employment.getDepartId()));
    }
}
