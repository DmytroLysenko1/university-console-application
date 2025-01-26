package org.example.dto.lector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.Degree;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectorDTO {
    private Long id;
    private String name;
    private Degree degree;
    private BigDecimal salary;
}
