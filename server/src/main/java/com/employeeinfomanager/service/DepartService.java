package com.employeeinfomanager.service;

import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.PageDto;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.DepartDao;
import com.employeeinfomanager.dao.EmploymentDao;
import com.employeeinfomanager.dao.UserDao;
import com.employeeinfomanager.dao.bo.Depart;
import com.employeeinfomanager.service.dto.DepartDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartService {

    private final DepartDao departDao;
    private final UserDao userDao;
    private final EmploymentDao employmentDao;

    @Autowired
    public DepartService(DepartDao departDao, UserDao userDao, EmploymentDao employmentDao) {
        this.departDao = departDao;
        this.userDao = userDao;
        this.employmentDao = employmentDao;
    }

    @Transactional
    public void createDepart(String name) {
        Depart depart = new Depart(null, name);
        this.departDao.insert(depart);
    }

    @Transactional
    public void deleteDepart(Long id) {
        if (!this.userDao.retrieveByDepartId(id).isEmpty()) {
            throw new BusinessException(ReturnNo.DEPART_STILL_IN_USE, String.format(ReturnNo.DEPART_STILL_IN_USE.getMessage(), id));
        }
        if (!this.employmentDao.retrieveEmploymentsByDepartId(id, 1, 1).isEmpty()) {
            throw new BusinessException(ReturnNo.DEPART_STILL_IN_USE, String.format(ReturnNo.DEPART_STILL_IN_USE.getMessage(), id));
        }
        this.departDao.deleteById(id);
    }

    @Transactional
    public void updateDepart(Long id, String name) {
        Depart depart = this.departDao.findById(id);
        depart.setName(name);
        this.departDao.save(depart);
    }

    @Transactional
    public PageDto<DepartDto> retrieveDeparts(int page, int pageSize) {
        List<DepartDto> departs = this.departDao.retrieveAll(page, pageSize).stream().map(DepartService::getDto).toList();
        return new PageDto<>(departs, page, departs.size(), this.departDao.getDepartCount());
    }

    public static DepartDto getDto(Depart bo) {
        return new DepartDto(bo.getId(), bo.getName());
    }
}
