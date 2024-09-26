package com.employeeinfomanager.service;

import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.JwtHelper;
import com.employeeinfomanager.common.PageDto;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.UserDao;
import com.employeeinfomanager.dao.bo.User;
import com.employeeinfomanager.service.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;

    @Value("${EmployeeInfoManager.token.expire.seconds}")
    private int TOKEN_EXPIRE_SECONDS;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(String username, String password, Long departId) {
        User user = new User(username, password, AuditLevel.ADMIN, departId);
        this.userDao.insert(user);
    }

    public String login(String username, String password) {
        User user = this.userDao.findByUsername(username);
        if (!password.equals(user.getPassword())) {
            throw new BusinessException(ReturnNo.AUTH_INVALID_ACCOUNT);
        }

        JwtHelper jwtHelper = new JwtHelper();
        return jwtHelper.createToken(user.getId(), user.getUsername(), user.getLevel(), user.getDepartId(), TOKEN_EXPIRE_SECONDS);
    }

    public PageDto<UserDto> retrieveUsers(int page, int pageSize) {
        List<User> users = this.userDao.retrieveAll(page, pageSize);
        List<UserDto> result = users.stream().map(this::getDto).toList();
        return new PageDto<>(result, page, result.size());
    }

    public void deleteUser(Long userId) {
        User user = this.userDao.findById(userId);
        if (user.getLevel() == AuditLevel.ROOT) {
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT);
        }

        this.userDao.deleteById(userId);
    }

    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = this.userDao.findById(userId);
        if (!user.getPassword().equals(oldPassword)) {
            throw new BusinessException(ReturnNo.OLD_PASSWORD_INCORRECT);
        }
        user.setPassword(newPassword);
        this.userDao.save(user);
    }

    private UserDto getDto(User bo) {
        return new UserDto(bo.getId(), bo.getUsername(), bo.getLevel(), bo.getDepart().getName());
    }
}
