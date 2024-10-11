package com.employeeinfomanager.controller;

import com.employeeinfomanager.aop.Audit;
import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.common.BusinessException;
import com.employeeinfomanager.common.ReturnNo;
import com.employeeinfomanager.common.ReturnObject;
import com.employeeinfomanager.common.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final String STAFF_IMAGE_TYPE = "staff";
    private final String WORK_PERMIT_TYPE = "workPermit";
    @Value("${EmployeeInfoManager.image-path.staff}")
    private String STAFF_IMAGE_PATH;
    @Value("${EmployeeInfoManager.image-path.work-permit}")
    private String WORK_PERMIT_PATH;

    @PutMapping("/upload/{type}")
    @Audit(AuditLevel.ADMIN)
    public ReturnObject uploadImage(@PathVariable String type, @RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return new ReturnObject(ReturnNo.IMAGE_EMPTY);
        }
        String path = getBasePathByImageType(type);
        String filename;
        try {
            filename = Utils.getMD5(file.getBytes());
        } catch (IOException e) {
            return new ReturnObject(ReturnNo.FAILED_READING_IMAGE);
        }
        File output_file = new File(path + filename);
        if (!output_file.getParentFile().exists() && !output_file.getParentFile().mkdirs()) {
            return new ReturnObject(ReturnNo.FAILED_WRITING_IMAGE);
        }
        try {
            file.transferTo(output_file);
        } catch (IOException e) {
            return new ReturnObject(ReturnNo.FAILED_WRITING_IMAGE);
        }
        return new ReturnObject(ReturnNo.CREATED, ReturnNo.CREATED.getMessage(), filename);
    }

    @GetMapping("/{type}/{filename}")
    public ReturnObject downloadImage(@PathVariable String type, @PathVariable String filename) {
        String basePath = getBasePathByImageType(type);
        Path filePath = Paths.get(basePath).resolve(filename).normalize();
        try {
            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return new ReturnObject(ReturnNo.OK, resource);
            } else {
                return new ReturnObject(ReturnNo.IMAGE_NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            return new ReturnObject(ReturnNo.FAILED_READING_IMAGE);
        }
    }

    private String getBasePathByImageType(String type) {
        if (STAFF_IMAGE_TYPE.equals(type))
            return STAFF_IMAGE_PATH;
        if (WORK_PERMIT_TYPE.equals(type))
            return WORK_PERMIT_PATH;
        throw new BusinessException(ReturnNo.INVALID_IMAGE_TYPE, String.format(ReturnNo.INVALID_IMAGE_TYPE.getMessage(), type));
    }
}
