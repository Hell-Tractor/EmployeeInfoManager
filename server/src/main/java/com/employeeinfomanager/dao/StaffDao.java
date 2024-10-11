package com.employeeinfomanager.dao;

import com.employeeinfomanager.Mapper.StaffPoMapper;
import com.employeeinfomanager.Mapper.po.StaffPo;
import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.bo.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StaffDao {
    private final StaffPoMapper staffPoMapper;

    @Autowired
    public StaffDao(StaffPoMapper staffPoMapper) {
        this.staffPoMapper = staffPoMapper;
    }

    private Staff getBo(StaffPo po) {
        return Staff.builder().id(po.getId()).name(po.getName()).image(po.getImage()).bornYear(po.getBornYear())
                .personId(po.getPersonId()).experience(po.getExperience()).physicalCondition(po.getPhysicalCondition())
                .appendix(po.getAppendix()).build();
    }

    private StaffPo getPo(Staff bo) {
        return StaffPo.builder().id(bo.getId()).name(bo.getName()).image(bo.getImage()).bornYear(bo.getBornYear())
                .personId(bo.getPersonId()).experience(bo.getExperience()).physicalCondition(bo.getPhysicalCondition())
                .appendix(bo.getAppendix()).build();
    }

    public Staff findById(Long id) {
        Optional<StaffPo> po = this.staffPoMapper.findById(id);
        if (po.isEmpty())
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "员工", id));
        return getBo(po.get());
    }

    public Staff findByPersonId(String personId) {
        Optional<StaffPo> po = this.staffPoMapper.findByPersonId(personId);
        if (po.isEmpty())
            throw new BusinessException(ReturnNo.STAFF_NOT_EXIST, String.format(ReturnNo.STAFF_NOT_EXIST.getMessage(), personId));
        return getBo(po.get());
    }

    public void insert(Staff staff) {
        Optional<StaffPo> po = this.staffPoMapper.findByPersonId(staff.getPersonId());
        if (po.isPresent())
            throw new BusinessException(ReturnNo.STAFF_EXIST, String.format(ReturnNo.STAFF_EXIST.getMessage(), staff.getPersonId()));

        StaffPo newStaffPo = getPo(staff);
        newStaffPo.setId(null);
        this.staffPoMapper.save(newStaffPo);
        staff.setId(newStaffPo.getId());
    }

    public void save(Staff staff) {
        if (null == staff.getId())
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "员工", -1));
        this.findById(staff.getId()); // check if staff exists
        this.staffPoMapper.save(getPo(staff));
    }

    public void deleteById(Long id) {
        this.findById(id);  // check if staff exists
        this.staffPoMapper.deleteById(id);
    }
}
