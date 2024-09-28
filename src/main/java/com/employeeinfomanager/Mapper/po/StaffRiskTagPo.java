package com.employeeinfomanager.Mapper.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "staff_risk_tag")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffRiskTagPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long staffId;
    private Long riskTagId;
}
