package com.employeeinfomanager.service;

import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.PageDto;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.DepartDao;
import com.employeeinfomanager.dao.UserDao;
import com.employeeinfomanager.dao.bo.Depart;
import com.employeeinfomanager.service.dto.DepartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartService {

    private final DepartDao departDao;
    private final UserDao userDao;

    @Autowired
    public DepartService(DepartDao departDao, UserDao userDao) {
        this.departDao = departDao;
        this.userDao = userDao;
    }

    public void createDepart(String name) {
        Depart depart = new Depart(null, name);
        this.departDao.insert(depart);
    }

    public void deleteDepart(Long id) {
        if (!this.userDao.retrieveByDepartId(id).isEmpty()) {
            throw new BusinessException(ReturnNo.DEPART_STILL_IN_USE, String.format(ReturnNo.DEPART_STILL_IN_USE.getMessage(), id));
        }
        // todo: check if depart is used in any staff
        this.departDao.deleteById(id);
    }

    public void updateDepart(Long id, String name) {
        Depart depart = this.departDao.findById(id);
        depart.setName(name);
        this.departDao.save(depart);
    }

    public PageDto<DepartDto> retrieveDeparts(int page, int pageSize) {
        List<DepartDto> departs = this.departDao.retrieveAll(page, pageSize).stream().map(this::getDto).toList();
        return new PageDto<>(departs, page, departs.size());
    }

    private DepartDto getDto(Depart bo) {
        return new DepartDto(bo.getId(), bo.getName());
    }
}
