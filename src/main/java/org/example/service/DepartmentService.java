package org.example.service;

import org.example.dto.depatrment.DepartmentRequestDTO;
import org.example.entity.Lector;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public interface DepartmentService {
    Optional<DepartmentRequestDTO> findByName(String name);
    BigDecimal fetchAverageSalary(String departmentName);
    Integer fetchAssistantsCount(String departmentName);
    Integer fetchAssociateProfessorsCount(String departmentName);
    Integer fetchProfessorsCount(String departmentName);
    String fetchHeadOfDepartment(String departmentName);
    Integer fetchEmployeeCount(String departmentName);
    List<Lector> searchLecturersByNameContaining(String template);
}
