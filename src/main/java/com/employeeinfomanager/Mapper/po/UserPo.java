package com.employeeinfomanager.Mapper.po;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "User")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPo {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private int level;

    // foreign key
    private Long departId;
}
