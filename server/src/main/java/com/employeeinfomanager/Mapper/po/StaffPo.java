package com.employeeinfomanager.Mapper.po;

import jakarta.persistence.*;
import lombok.*;

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
    private String experience;
    private String physicalCondition;
    private String appendix;
}
