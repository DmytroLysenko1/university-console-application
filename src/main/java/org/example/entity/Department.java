package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.annotations.EntityEqualsAndHashCode;
import org.example.entity.abstracts.OrganizationalUnit;
import java.util.Set;

@Entity
@Table(name = "departments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityEqualsAndHashCode
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class Department extends OrganizationalUnit {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "department_lector",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "lector_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private Set<Lector> employees;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "head_of_department_id")
    private Lector headOfDepartment;
}
