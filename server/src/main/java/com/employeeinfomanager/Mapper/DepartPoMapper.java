package com.employeeinfomanager.Mapper;

import com.employeeinfomanager.Mapper.po.DepartPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartPoMapper extends JpaRepository<DepartPo, Long> {
    Optional<DepartPo> findByName(String name);
}
