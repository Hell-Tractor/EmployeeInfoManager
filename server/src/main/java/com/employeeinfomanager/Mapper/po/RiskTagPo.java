package com.employeeinfomanager.Mapper.po;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "risk_tag")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiskTagPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
