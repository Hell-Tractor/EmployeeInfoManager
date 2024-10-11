package com.employeeinfomanager.Mapper.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmploymentPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long staffId;
    private Long departId;
    private String project;
    private LocalDate validSince;
    private LocalDate validUntil;
    private String workPermit;
    private String violation;
}
