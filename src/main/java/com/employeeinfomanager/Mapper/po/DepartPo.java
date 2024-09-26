package com.employeeinfomanager.Mapper.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "depart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
