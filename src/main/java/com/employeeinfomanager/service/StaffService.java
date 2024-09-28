package com.employeeinfomanager.service;

import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.controller.vo.CreateStaffVo;
import com.employeeinfomanager.controller.vo.UpdateStaffVo;
import com.employeeinfomanager.dao.EmploymentDao;
import com.employeeinfomanager.dao.StaffDao;
import com.employeeinfomanager.dao.bo.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffService {
    private final StaffDao staffDao;
    private final EmploymentDao employmentDao;

    @Autowired
    public StaffService(StaffDao staffDao, EmploymentDao employmentDao) {
        this.staffDao = staffDao;
        this.employmentDao = employmentDao;
    }

    private Staff getBo(CreateStaffVo vo) {
        return Staff.builder().id(null).name(vo.getName()).image(vo.getImage()).personId(vo.getPersonId())
                .bornYear(vo.getBornYear()).build();
    }

    private Staff getBo(UpdateStaffVo vo) {
        return Staff.builder().id(vo.getId()).name(vo.getName()).image(vo.getImage()).personId(vo.getPersonId())
                .bornYear(vo.getBornYear()).build();
    }

    public void createStaff(CreateStaffVo staff) {
        this.staffDao.insert(getBo(staff));
    }

    public void deleteStaff(Long id) {
        // check if staff is used by employment
        if (!this.employmentDao.retrieveEmploymentIdsByStaffId(id).isEmpty())
            throw new BusinessException(ReturnNo.STAFF_STILL_IN_USE, String.format(ReturnNo.STAFF_STILL_IN_USE.getMessage(), id));
        this.staffDao.deleteById(id);
    }

    public void updateStaff(UpdateStaffVo vo) {
        this.staffDao.save(getBo(vo));
    }

    public Staff findByPersonId(String personId) {
        return this.staffDao.findByPersonId(personId);
    }
}
