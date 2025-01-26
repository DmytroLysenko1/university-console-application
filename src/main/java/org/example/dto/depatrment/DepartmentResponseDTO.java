package org.example.dto.depatrment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.lector.LectorDTO;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDTO {
    private Long id;
    private String departmentName;
    private Set<LectorDTO> employees;
    private LectorDTO headOfDepartment;
}
