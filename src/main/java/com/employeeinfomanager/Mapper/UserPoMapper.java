package com.employeeinfomanager.Mapper;

import com.employeeinfomanager.Mapper.po.UserPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPoMapper extends JpaRepository<UserPo, Long> {
    Optional<UserPo> findByUsername(String username);
    List<UserPo> findAllByDepartId(Long departId);
}
