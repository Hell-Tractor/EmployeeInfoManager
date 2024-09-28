package com.employeeinfomanager.Mapper;

import com.employeeinfomanager.Mapper.po.StaffPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffPoMapper extends JpaRepository<StaffPo, Long> {
}
