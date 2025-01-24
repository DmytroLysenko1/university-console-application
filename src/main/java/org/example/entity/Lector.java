package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.entity.abstracts.UniversityMember;

import java.util.Set;

@Entity
@Table(name = "lectors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Lector extends UniversityMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(mappedBy = "employees")
    private Set<Department> department;
}

