package com.employeeinfomanager.service;

import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.*;
import com.employeeinfomanager.dao.UserDao;
import com.employeeinfomanager.dao.bo.User;
import com.employeeinfomanager.service.dto.LoginDto;
import com.employeeinfomanager.service.dto.UserDto;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void createUser(String username, String password, Long departId) {
        User user = new User(username, password, AuditLevel.ADMIN, departId);
        this.userDao.insert(user);
    }

    @Transactional
    public String getLoginSalt(String username) {
        User user = this.userDao.findByUsername(username);
        user.setSalt(Utils.getRandomSalt());
        this.userDao.save(user);
        return user.getSalt();
    }

    @Transactional
    public LoginDto login(String username, String password, String salt) {
        User user = this.userDao.findByUsername(username);
        if (!salt.equals(user.getSalt())) {
            throw new BusinessException(ReturnNo.BAD_SALT);
        }
        String md5Password = Utils.getMD5((user.getPassword() + user.getSalt()).getBytes());
        if (!password.equals(md5Password)) {
            throw new BusinessException(ReturnNo.AUTH_INVALID_ACCOUNT);
        }

        JwtHelper jwtHelper = new JwtHelper();
        String token = jwtHelper.createToken(user.getId(), user.getUsername(), user.getLevel(), user.getDepartId(), TOKEN_EXPIRE_SECONDS);
        return new LoginDto(token, user.getLevel());
    }

    @Transactional
    public PageDto<UserDto> retrieveUsers(int page, int pageSize) {
        List<User> users = this.userDao.retrieveAll(page, pageSize);
        List<UserDto> result = users.stream().map(this::getDto).toList();
        return new PageDto<>(result, page, result.size(), this.userDao.getUserCount());
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = this.userDao.findById(userId);
        if (user.getLevel() == AuditLevel.ROOT) {
            throw new BusinessException(ReturnNo.AUTH_NO_RIGHT);
        }

        this.userDao.deleteById(userId);
    }

    @Transactional
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
