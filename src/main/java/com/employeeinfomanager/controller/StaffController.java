package com.employeeinfomanager.controller;

import com.employeeinfomanager.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/data", produces = "application/json;charset=UTF-8")
public class StaffController {
    private Logger logger = LoggerFactory.getLogger(StaffController.class);

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }


}
