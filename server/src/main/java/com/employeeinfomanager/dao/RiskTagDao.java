package com.employeeinfomanager.dao;

import com.employeeinfomanager.Mapper.EmploymentPoMapper;
import com.employeeinfomanager.Mapper.EmploymentRiskTagPoMapper;
import com.employeeinfomanager.Mapper.RiskTagPoMapper;
import com.employeeinfomanager.Mapper.po.EmploymentRiskTagPo;
import com.employeeinfomanager.Mapper.po.RiskTagPo;
import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.bo.RiskTag;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RiskTagDao {
    private final RiskTagPoMapper riskTagPoMapper;
    private final EmploymentRiskTagPoMapper employmentRiskTagPoMapper;
    private final EmploymentPoMapper employmentPoMapper;

    public RiskTagDao(RiskTagPoMapper riskTagPoMapper, EmploymentRiskTagPoMapper employmentRiskTagPoMapper, EmploymentPoMapper employmentPoMapper) {
        this.riskTagPoMapper = riskTagPoMapper;
        this.employmentRiskTagPoMapper = employmentRiskTagPoMapper;
        this.employmentPoMapper = employmentPoMapper;
    }

    private RiskTag getBo(RiskTagPo po) {
        return new RiskTag(po.getId(), po.getName());
    }

    private RiskTagPo getPo(RiskTag bo) {
        return new RiskTagPo(bo.getId(), bo.getName());
    }

    public List<RiskTag> retrieveByEmploymentId(Long employmentId) {
        return this.employmentRiskTagPoMapper.findAllByEmploymentId(employmentId).stream()
                .map(item -> this.findById(item.getRiskTagId()))
                .toList();
    }

    public List<RiskTag> retrieveAll(int page, int pageSize) {
        return this.riskTagPoMapper.findAll(PageRequest.of(page - 1, pageSize)).stream().map(this::getBo).toList();
    }

    public Long getRiskTagCount() {
        return this.riskTagPoMapper.count();
    }

    public RiskTag findById(Long id) {
        Optional<RiskTagPo> po = this.riskTagPoMapper.findById(id);
        if (po.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "风险", id));
        }
        return getBo(po.get());
    }

    public void insert(RiskTag tag) {
        Optional<RiskTagPo> po = this.riskTagPoMapper.findByName(tag.getName());
        if (po.isPresent()) {
            throw new BusinessException(ReturnNo.RISK_TAG_EXIST, String.format(ReturnNo.RISK_TAG_EXIST.getMessage(), tag.getName()));
        }

        RiskTagPo newTagPo = getPo(tag);
        newTagPo.setId(null);
        this.riskTagPoMapper.save(newTagPo);
        tag.setId(newTagPo.getId());
    }

    public void save(RiskTag tag) {
        if (null == tag.getId()) {
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "风险标签", -1));
        }
        this.findById(tag.getId());
        this.riskTagPoMapper.save(getPo(tag));
    }

    public void deleteById(Long id) {
        if (null == id) {
            throw new BusinessException(ReturnNo.PARAMETER_MISSED, String.format(ReturnNo.PARAMETER_MISSED.getMessage(), "riskTagId"));
        }
        this.findById(id);
        this.riskTagPoMapper.deleteById(id);
    }

    public void addRiskTagToEmployment(Long employmentId, Long riskTagId) {
        if (!this.employmentPoMapper.existsById(employmentId))
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "雇佣", employmentId));
        RiskTag riskTag = this.findById(riskTagId);

        if (this.employmentRiskTagPoMapper.findByEmploymentIdAndRiskTagId(employmentId, riskTagId).isPresent())
            throw new BusinessException(ReturnNo.RISK_TAG_ALREADY_IN_EMPLOYMENT, String.format(ReturnNo.RISK_TAG_ALREADY_IN_EMPLOYMENT.getMessage(), riskTag.getName(), employmentId));
        EmploymentRiskTagPo po = new EmploymentRiskTagPo(null, employmentId, riskTagId);
        this.employmentRiskTagPoMapper.save(po);
    }

    public void removeRiskTagFromEmployment(Long employmentId, Long riskTagId) {
        RiskTag tag = this.findById(riskTagId);
        Optional<EmploymentRiskTagPo> po = this.employmentRiskTagPoMapper.findByEmploymentIdAndRiskTagId(employmentId, riskTagId);
        if (po.isEmpty())
            throw new BusinessException(ReturnNo.RISK_TAG_NOT_FOUND_IN_EMPLOYMENT, String.format(ReturnNo.RISK_TAG_NOT_FOUND_IN_EMPLOYMENT.getMessage(), employmentId, tag.getName()));

        this.employmentRiskTagPoMapper.deleteById(po.get().getId());
    }
}
