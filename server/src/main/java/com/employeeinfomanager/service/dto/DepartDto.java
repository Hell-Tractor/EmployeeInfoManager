package com.employeeinfomanager.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude
@AllArgsConstructor
@NoArgsConstructor
public class DepartDto {
    private Long id;
    private String name;
}
