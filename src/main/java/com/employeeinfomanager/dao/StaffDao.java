package com.employeeinfomanager.dao;

import com.employeeinfomanager.Mapper.StaffPoMapper;
import com.employeeinfomanager.Mapper.StaffRiskTagPoMapper;
import com.employeeinfomanager.dao.bo.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StaffDao {
    private final StaffPoMapper staffPoMapper;
    private final StaffRiskTagPoMapper staffRiskTagPoMapper;
    private final DepartDao departDao;
    private final RiskTagDao riskTagDao;

    @Autowired
    public StaffDao(StaffPoMapper staffPoMapper, StaffRiskTagPoMapper staffRiskTagPoMapper, DepartDao departDao, RiskTagDao riskTagDao) {
        this.staffPoMapper = staffPoMapper;
        this.staffRiskTagPoMapper = staffRiskTagPoMapper;
        this.departDao = departDao;
        this.riskTagDao = riskTagDao;
    }

    public List<Long> retrieveStaffIdByRiskTagId(Long riskTagId) {
        return this.staffRiskTagPoMapper.findDistinctStaffIdByRiskTagId(riskTagId).stream()
                .toList();
    }
}
