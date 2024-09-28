package com.employeeinfomanager.Mapper.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "staff")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    private int bornYear;
    private String personId;
    private Long departId;  // foreign key
    private String project;
    private LocalDate validSince;
    private LocalDate validUntil;
    private String workPermit;
    private String experience;
    private String physicalCondition;
    private String violation;
    private String appendix;
}
