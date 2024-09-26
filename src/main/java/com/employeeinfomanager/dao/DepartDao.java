package com.employeeinfomanager.dao;

import com.employeeinfomanager.Mapper.DepartPoMapper;
import com.employeeinfomanager.Mapper.po.DepartPo;
import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.dao.bo.Depart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DepartDao {
    private final DepartPoMapper mapper;

    @Autowired
    public DepartDao(DepartPoMapper mapper) {
        this.mapper = mapper;
    }

    private Depart getBo(DepartPo po) {
        return new Depart(po.getId(), po.getName());
    }

    private DepartPo getPo(Depart bo) {
        return new DepartPo(bo.getId(), bo.getName());
    }

    public Depart findById(Long id) {
        Optional<DepartPo> po = this.mapper.findById(id);
        if (po.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "公司", id));
        }
        return getBo(po.get());
    }

    public void insert(Depart depart) {
        Optional<DepartPo> po = this.mapper.findByName(depart.getName());
        if (po.isPresent()) {
            throw new BusinessException(ReturnNo.DEPART_EXIST, String.format(ReturnNo.DEPART_EXIST.getMessage(), depart.getName()));
        }

        DepartPo newDepartPo = getPo(depart);
        newDepartPo.setId(null);
        this.mapper.save(newDepartPo);
        depart.setId(newDepartPo.getId());
    }

    public void deleteById(Long id) {
        if (null == id) {
            throw new BusinessException(ReturnNo.PARAMETER_MISSED, String.format(ReturnNo.PARAMETER_MISSED.getMessage(), "departId"));
        }
        this.findById(id);   // check if depart exists
        this.mapper.deleteById(id);
    }

    public void save(Depart depart) {
        if (null == depart.getId()) {
            throw new BusinessException(ReturnNo.RESOURCE_NOT_EXIST, String.format(ReturnNo.RESOURCE_NOT_EXIST.getMessage(), "公司", -1));
        }
        this.findById(depart.getId()); // check if depart exists;
        this.mapper.save(getPo(depart));
    }

    public List<Depart> retrieveAll(int page, int pageSize) {
        return this.mapper.findAll(PageRequest.of(page - 1, pageSize)).stream().map(this::getBo).toList();
    }
}
