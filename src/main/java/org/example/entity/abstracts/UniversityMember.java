package org.example.entity.abstracts;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.Degree;

import java.math.BigDecimal;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public abstract class UniversityMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Degree degree;
    private BigDecimal salary;
}
