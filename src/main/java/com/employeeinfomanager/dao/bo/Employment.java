package com.employeeinfomanager.dao.bo;

import com.employeeinfomanager.dao.DepartDao;
import com.employeeinfomanager.dao.RiskTagDao;
import com.employeeinfomanager.dao.StaffDao;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
public class Employment {
    @Getter
    private Long id;
    @Getter
    private Long staffId;
    private Staff staff;
    @Setter
    private StaffDao staffDao;
    public Staff getStaff() {
        if (null == this.staffId)
            return null;
        if (null == this.staff && null != this.staffDao)
            this.staff = this.staffDao.findById(this.staffId);
        return this.staff;
    }
    public void setStaffId(Long staffId) {
        if (staffId == this.staffId)
            return;
        this.staffId = staffId;
        this.staff = null;
    }

    @Getter
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
    public void setDepartId(Long departId) {
        if (departId == this.departId)
            return;
        this.departId = departId;
        this.depart = null;
    }

    @Getter @Setter
    private String project;
    @Getter @Setter
    private LocalDate validSince;
    @Getter @Setter
    private LocalDate validUntil;
    @Getter @Setter
    private String workPermit;
    @Getter @Setter
    private String violation;

    private List<RiskTag> riskTags;
    @Setter
    private RiskTagDao riskTagDao;
    public List<RiskTag> getRiskTags() {
        if (null == this.id)
            return null;
        if (null == this.riskTags && null != this.riskTagDao)
            this.riskTags = this.riskTagDao.retrieveByEmploymentId(this.id);
        return this.riskTags;
    }
    public void setId(Long id) {
        if (id == this.id)
            return;
        this.id = id;
        this.riskTags = null;
    }
}
