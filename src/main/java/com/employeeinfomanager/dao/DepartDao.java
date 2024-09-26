package com.employeeinfomanager.dao;

import com.employeeinfomanager.dao.bo.Depart;
import org.springframework.stereotype.Repository;

@Repository
public class DepartDao {

    public Depart findById(Long id) {
        // todo
        return new Depart(id, "test depart");
    }
}
