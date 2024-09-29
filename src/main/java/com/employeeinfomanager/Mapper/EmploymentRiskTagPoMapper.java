package com.employeeinfomanager.Mapper;

import com.employeeinfomanager.Mapper.po.EmploymentRiskTagPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmploymentRiskTagPoMapper extends JpaRepository<EmploymentRiskTagPo, Long> {
    List<EmploymentRiskTagPo> findAllByEmploymentId(Long employmentId);
    @Query("SELECT DISTINCT s.employmentId FROM EmploymentRiskTagPo s WHERE s.riskTagId = :riskTagId")
    List<Long> findDistinctEmploymentIdByRiskTagId(Long riskTagId);
    Optional<EmploymentRiskTagPo> findByEmploymentIdAndRiskTagId(Long employmentId, Long riskTagId);
}
