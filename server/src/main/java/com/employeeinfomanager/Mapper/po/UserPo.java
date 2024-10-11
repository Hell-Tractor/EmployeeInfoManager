package com.employeeinfomanager.Mapper.po;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private int level;
    private String salt;

    // foreign key
    private Long departId;
}
