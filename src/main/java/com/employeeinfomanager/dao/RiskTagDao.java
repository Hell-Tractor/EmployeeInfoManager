package com.employeeinfomanager.dao;

import com.employeeinfomanager.Mapper.RiskTagPoMapper;
import com.employeeinfomanager.Mapper.StaffRiskTagPoMapper;
import com.employeeinfomanager.Mapper.po.RiskTagPo;
import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.bo.RiskTag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RiskTagDao {
    private final RiskTagPoMapper riskTagPoMapper;
    private final StaffRiskTagPoMapper staffRiskTagPoMapper;

    public RiskTagDao(RiskTagPoMapper riskTagPoMapper, StaffRiskTagPoMapper staffRiskTagPoMapper) {
        this.riskTagPoMapper = riskTagPoMapper;
        this.staffRiskTagPoMapper = staffRiskTagPoMapper;
    }

    private RiskTag getBo(RiskTagPo po) {
        return new RiskTag(po.getId(), po.getName());
    }

    private RiskTagPo getPo(RiskTag bo) {
        return new RiskTagPo(bo.getId(), bo.getName());
    }

    public List<RiskTag> retrieveByStaffId(Long staffId) {
        return this.staffRiskTagPoMapper.findAllByStaffId(staffId).stream()
                .map(item -> this.findById(item.getId()))
                .toList();
    }

    public List<RiskTag> retrieveAll() {
        return this.riskTagPoMapper.findAll().stream().map(this::getBo).toList();
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
}
