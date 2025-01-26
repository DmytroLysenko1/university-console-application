package org.example.dto.depatrment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequestDTO {
    private String departmentName;
    private Set<Long> employeesIds;
    private Long headOfDepartmentId;
}
