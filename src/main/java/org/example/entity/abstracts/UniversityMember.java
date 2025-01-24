package org.example.entity.abstracts;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.Degree;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public abstract class UniversityMember {
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Degree degree;
    private BigDecimal salary;
    private Boolean isHeadOfDepartment;
}
