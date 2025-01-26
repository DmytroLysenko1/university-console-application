package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.depatrment.DepartmentRequestDTO;
import org.example.entity.Department;
import org.example.entity.Lector;
import org.example.enums.Degree;
import org.example.repositrory.DepartmentRepository;
import org.example.repositrory.LectorRepository;
import org.example.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final LectorRepository lectorRepository;

    @Override
    public Optional<DepartmentRequestDTO> findByName(String name) {
        return this.departmentRepository.findByName(name).map( department -> {
            DepartmentRequestDTO departmentRequestDTO = new DepartmentRequestDTO();
            departmentRequestDTO.setDepartmentName(department.getDepartmentName());
            departmentRequestDTO.setHeadOfDepartmentId(department.getHeadOfDepartment().getId());
            return departmentRequestDTO;
        });
    }

    @Override
    public BigDecimal fetchAverageSalary(String departmentName) {
        return this.lectorRepository.fetchAverageSalaryByDepartmentName(departmentName);
    }

    @Override
    public Integer fetchAssistantsCount(String departmentName) {
        return fetchLectorsByDegreeAndDepartment(departmentName, Degree.ASSISTANT).size();
    }

    @Override
    public Integer fetchAssociateProfessorsCount(String departmentName) {
        return fetchLectorsByDegreeAndDepartment(departmentName, Degree.ASSOCIATE_PROFESSOR).size();
    }

    @Override
    public Integer fetchProfessorsCount(String departmentName) {
        return fetchLectorsByDegreeAndDepartment(departmentName, Degree.PROFESSOR).size();
    }

    @Override
    public String fetchHeadOfDepartment(String departmentName) {
        return this.departmentRepository
                .findByName(departmentName)
                .map(department -> department.getHeadOfDepartment().getName())
                .orElse("Head of department not found");
    }

    @Override
    public Integer fetchEmployeeCount(String departmentName) {
        Department department = this.departmentRepository.findByName(departmentName).orElseThrow();
        return department.getEmployees().size();
    }

    @Override
    public List<Lector> searchLecturersByNameContaining(String name) {
        return lectorRepository.findByNameContaining(name);
    }

    private List<Lector> fetchLectorsByDegreeAndDepartment(String departmentName, Degree degree) {
        Department department = this.departmentRepository.findByName(departmentName).orElseThrow();
        return department
                .getEmployees()
                .stream()
                .filter(lector -> lector.getDegree().equals(degree))
                .toList();
    }
}
