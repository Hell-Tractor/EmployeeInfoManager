package com.employeeinfomanager.Mapper;

import com.employeeinfomanager.Mapper.po.RiskTagPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskTagPoMapper extends JpaRepository<RiskTagPo, Long> {
    Optional<RiskTagPo> findByName(String name);
}
