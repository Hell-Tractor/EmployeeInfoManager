package com.employeeinfomanager.dao;

import com.employeeinfomanager.Mapper.UserPoMapper;
import com.employeeinfomanager.Mapper.po.UserPo;
import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.bo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {
    private final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private final UserPoMapper mapper;
    private final DepartDao departDao;

    @Autowired
    public UserDao(UserPoMapper mapper, DepartDao departDao) {
        this.mapper = mapper;
        this.departDao = departDao;
    }

    private void setBo(User bo) {
        bo.setDepartDao(this.departDao);
    }

    private User getBo(UserPo po) {
        User bo = User.builder().id(po.getId()).username(po.getUsername()).password(po.getPassword()).salt(po.getSalt())
                .level(AuditLevel.values()[po.getLevel()]).departId(po.getDepartId()).build();
        this.setBo(bo);
        return bo;
    }

    private UserPo getPo(User bo) {
        return UserPo.builder().id(bo.getId()).username(bo.getUsername()).password(bo.getPassword()).salt(bo.getSalt())
                .level(bo.getLevel().ordinal()).departId(bo.getDepartId()).build();
    }

    public List<User> retrieveAll(int page, int pageSize) {
        return this.mapper.findAll(PageRequest.of(page - 1, pageSize)).stream().map(this::getBo).toList();
    }

    public Long getUserCount() {
        return this.mapper.count();
    }

    public List<User> retrieveByDepartId(Long id) {
        return this.mapper.findAllByDepartId(id).stream().map(this::getBo).toList();
    }

    public User findById(Long id) {
        Optional<UserPo> po = this.mapper.findById(id);
        if (po.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "用户", id));
        }
        return getBo(po.get());
    }

    public User findByUsername(String username) {
        Optional<UserPo> po = this.mapper.findByUsername(username);
        if (po.isEmpty()) {
            throw new BusinessException(ReturnNo.USER_NOT_EXIST, String.format(ReturnNo.USER_NOT_EXIST.getMessage(), username));
        }

        return getBo(po.get());
    }

    public void insert(User user) {
        // check if already exists
        Optional<UserPo> po = this.mapper.findByUsername(user.getUsername());
        if (po.isPresent()) {
            throw new BusinessException(ReturnNo.USER_EXIST, String.format(ReturnNo.USER_EXIST.getMessage(), user.getUsername()));
        }

        // check if depart is valid
        setBo(user);
        if (null == user.getDepart()) {
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "公司", user.getDepartId()));
        }

        UserPo newUserPo = getPo(user);
        newUserPo.setId(null);
        this.mapper.save(newUserPo);
        user.setId(newUserPo.getId());
    }

    public void save(User user) {
        if (null == user.getId()) {
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "用户", -1));
        }
        this.findById(user.getId()); // check if user exists

        // check if depart is valid
        setBo(user);
        if (null == user.getDepart()) {
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "公司", user.getDepartId()));
        }

        this.mapper.save(getPo(user));
    }

    public void deleteById(Long id) {
        if (null == id) {
            throw new BusinessException(ReturnNo.PARAMETER_MISSED, String.format(ReturnNo.PARAMETER_MISSED.getMessage(), "userId"));
        }
        this.findById(id);
        this.mapper.deleteById(id);
    }
}
