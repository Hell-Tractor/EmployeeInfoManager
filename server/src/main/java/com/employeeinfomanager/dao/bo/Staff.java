package com.employeeinfomanager.dao.bo;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    private Long id;
    private String name;
    private String image;
    private int bornYear;
    private String personId;
    private String experience;
    private String physicalCondition;
    private String appendix;
}
