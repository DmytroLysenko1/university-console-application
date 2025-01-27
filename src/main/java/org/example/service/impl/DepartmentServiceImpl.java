package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.annotations.Loggable;
import org.example.dto.depatrment.DepartmentRequestDTO;
import org.example.entity.Department;
import org.example.entity.Lector;
import org.example.enums.Degree;
import org.example.exceptionHandling.customExceptions.NotFoundException;
import org.example.repositrory.DepartmentRepository;
import org.example.repositrory.LectorRepository;
import org.example.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final LectorRepository lectorRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(
            readOnly = true,
            isolation = Isolation.READ_COMMITTED
    )
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public Optional<DepartmentRequestDTO> findByName(String departmentName) {
        return this.departmentRepository.findByDepartmentName(departmentName)
                .map(department ->
                this.modelMapper.map(department, DepartmentRequestDTO.class)
        );
    }

    @Override
    @Transactional(
            readOnly = true,
            isolation = Isolation.READ_COMMITTED
    )
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public BigDecimal fetchAverageSalary(String departmentName) {
        return this.lectorRepository.fetchAverageSalaryByDepartmentName(departmentName);
    }

    @Override
    @Transactional(
            readOnly = true,
            isolation = Isolation.READ_COMMITTED
    )
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public Integer fetchAssistantsCount(String departmentName) {
        return countLectorsByDegreeAndDepartment(departmentName, Degree.ASSISTANT);
    }

    @Override
    @Transactional(
            readOnly = true,
            isolation = Isolation.READ_COMMITTED
    )
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public Integer fetchAssociateProfessorsCount(String departmentName) {
        return countLectorsByDegreeAndDepartment(
                departmentName,
                Degree.ASSOCIATE_PROFESSOR
        );
    }

    @Override
    @Transactional(
            readOnly = true,
            isolation = Isolation.READ_COMMITTED
    )
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public Integer fetchProfessorsCount(String departmentName) {
       return countLectorsByDegreeAndDepartment(
               departmentName,
               Degree.PROFESSOR
       );
    }

    @Override
    @Transactional(
            readOnly = true,
            isolation = Isolation.READ_COMMITTED
    )
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public String fetchHeadOfDepartment(String departmentName) {
        return this.departmentRepository
                .findByDepartmentName(departmentName)
                .map(Department::getHeadOfDepartment)
                .map(Lector::getName)
                .orElseThrow(
                        () -> new NotFoundException(
                                "Head of department not found",
                                departmentName
                        )
                );
    }

    @Override
    @Transactional(
            readOnly = true,
            isolation = Isolation.READ_COMMITTED
    )
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public Integer fetchEmployeeCount(String departmentName) {
        Department department = this.departmentRepository.findByDepartmentName(departmentName)
                .orElseThrow(
                        () -> new NotFoundException(
                                "Department not found",
                                departmentName
                        )
        );
        return department
                .getEmployees()
                .size();
    }

    @Override
    @Transactional(
            readOnly = true,
            isolation = Isolation.READ_COMMITTED
    )
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public List<Lector> searchLecturersByNameContaining(String name) {
        return this.lectorRepository.findByNameContaining(name);
    }

    @Loggable(
            level = "DEBUG",
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    private List<Lector> fetchLectorsByDegreeAndDepartment(String departmentName, Degree degree) {
        Department department = this.departmentRepository.findByDepartmentName(departmentName)
                .orElseThrow(
                        () -> new NotFoundException(
                                "Department not found",
                                departmentName
                        )
        );
        return department
                .getEmployees()
                .stream()
                .filter(lector -> lector
                        .getDegree()
                        .equals(degree)
                )
                .toList();
    }

    @Loggable(
            level = "DEBUG",
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    private Integer countLectorsByDegreeAndDepartment(String departmentName, Degree degree) {
        return fetchLectorsByDegreeAndDepartment(
                departmentName,
                degree
        ).size();
    }
}
