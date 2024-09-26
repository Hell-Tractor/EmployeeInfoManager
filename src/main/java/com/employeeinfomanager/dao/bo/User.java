package com.employeeinfomanager.dao.bo;

import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.dao.DepartDao;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private AuditLevel level;

    @Getter @Setter
    private Long departId;
    private Depart depart;
    @Setter
    private DepartDao departDao;
    public Depart getDepart() {
        if (null == this.departId)
            return null;
        if (null == this.depart && null != this.departDao)
            this.depart = this.departDao.findById(this.departId);
        return this.depart;
    }

    public User(String username, String password, AuditLevel level, Long departId) {
        this.id = null;
        this.username = username;
        this.password = password;
        this.level = level;
        this.departId = departId;
    }
}
