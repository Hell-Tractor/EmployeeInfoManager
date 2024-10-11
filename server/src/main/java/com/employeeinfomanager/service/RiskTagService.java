package com.employeeinfomanager.service;

import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.EmploymentDao;
import com.employeeinfomanager.dao.RiskTagDao;
import com.employeeinfomanager.dao.bo.RiskTag;
import com.employeeinfomanager.service.dto.RiskTagDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskTagService {
    private final RiskTagDao riskTagDao;
    private final EmploymentDao employmentDao;

    @Autowired
    public RiskTagService(RiskTagDao riskTagDao, EmploymentDao employmentDao) {
        this.riskTagDao = riskTagDao;
        this.employmentDao = employmentDao;
    }

    private RiskTagDto getDto(RiskTag bo) {
        return new RiskTagDto(bo.getId(), bo.getName());
    }

    @Transactional
    public void createRiskTag(String name) {
        RiskTag tag = new RiskTag(null, name);
        this.riskTagDao.insert(tag);
    }

    @Transactional
    public void deleteRiskTag(Long id) {
        if (!this.employmentDao.retrieveDistinctEmploymentIdsByRiskTagId(id).isEmpty()) {
            throw new BusinessException(ReturnNo.RISK_TAG_STILL_IN_USE, String.format(ReturnNo.RISK_TAG_STILL_IN_USE.getMessage(), id));
        }
        this.riskTagDao.deleteById(id);
    }

    @Transactional
    public void updateRiskTag(Long id, String name) {
        RiskTag riskTag = this.riskTagDao.findById(id);
        riskTag.setName(name);
        this.riskTagDao.save(riskTag);
    }

    @Transactional
    public List<RiskTagDto> retrieveRiskTags() {
        return this.riskTagDao.retrieveAll().stream().map(this::getDto).toList();
    }
}
