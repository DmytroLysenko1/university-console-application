package org.example.dto.lector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.depatrment.DepartmentDTO;
import org.example.enums.Degree;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectorResponseDTO {
    private Long id;
    private String name;
    private Degree degree;
    private BigDecimal salary;
    private Boolean isHeadOfDepartment;
    private Set<DepartmentDTO> departments;
}
