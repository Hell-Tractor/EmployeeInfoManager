package com.employeeinfomanager.Mapper;

import com.employeeinfomanager.Mapper.po.StaffRiskTagPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRiskTagPoMapper extends JpaRepository<StaffRiskTagPo, Long> {
    List<StaffRiskTagPo> findAllByStaffId(Long staffId);
    @Query("SELECT DISTINCT s.staffId FROM StaffRiskTagPo s WHERE s.riskTagId = :riskTagId")
    List<Long> findDistinctStaffIdByRiskTagId(Long riskTagId);
}
