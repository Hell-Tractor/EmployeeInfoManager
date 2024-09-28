package com.employeeinfomanager.dao;

import com.employeeinfomanager.Mapper.EmploymentPoMapper;
import com.employeeinfomanager.Mapper.EmploymentRiskTagPoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmploymentDao {
    private final EmploymentPoMapper employmentPoMapper;
    private final EmploymentRiskTagPoMapper employmentRiskTagPoMapper;

    @Autowired
    public EmploymentDao(EmploymentPoMapper employmentPoMapper, EmploymentRiskTagPoMapper employmentRiskTagPoMapper) {
        this.employmentPoMapper = employmentPoMapper;
        this.employmentRiskTagPoMapper = employmentRiskTagPoMapper;
    }

    public List<Long> retrieveDistinctEmploymentIdsByRiskTagId(Long riskTagId) {
        return this.employmentRiskTagPoMapper.findDistinctEmploymentIdByRiskTagId(riskTagId);
    }

    public List<Long> retrieveEmploymentIdsByStaffId(Long staffId) {
        return this.employmentPoMapper.findAllEmploymentIdByStaffId(staffId);
    }
}
