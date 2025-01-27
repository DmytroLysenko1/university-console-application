package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.example.annotations.EntityEqualsAndHashCode;
import org.example.entity.abstracts.UniversityMember;
import java.util.Set;

@Entity
@Table(name = "lectors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityEqualsAndHashCode
@ToString(callSuper = true)
public class Lector extends UniversityMember {
    private Boolean isHeadOfDepartment;
    @ManyToMany(mappedBy = "employees", fetch = FetchType.EAGER)
    private Set<Department> department;
}

