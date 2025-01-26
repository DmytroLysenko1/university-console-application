package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.entity.abstracts.OrganizationalUnit;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Department extends OrganizationalUnit {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "department_lector",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "lector_id")
    )
    private Set<Lector> employees = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "head_of_department_id")
    private Lector headOfDepartment;
}
