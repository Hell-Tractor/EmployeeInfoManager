package com.employeeinfomanager.Mapper;

import com.employeeinfomanager.Mapper.po.EmploymentPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploymentPoMapper extends JpaRepository<EmploymentPo, Long> {
    @Query("SELECT e.id FROM EmploymentPo e WHERE e.staffId = :staffId")
    List<Long> findAllEmploymentIdByStaffId(Long staffId);
    @Query("SELECT e.violation FROM EmploymentPo e WHERE e.staffId = :staffId")
    List<String> findAllViolationByStaffId(Long staffId);
    List<EmploymentPo> findAllByDepartId(Long departId, Pageable pageable);
    Long countByDepartId(Long departId);
}
