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

/**
 * Implementation of the DepartmentService interface.
 * Provides methods for interacting with department data, including fetching department details,
 * calculating statistics, and performing searches.
 */
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final LectorRepository lectorRepository;
    private final ModelMapper modelMapper;

    /**
     * Finds a department by its name and returns a DepartmentRequestDTO.
     *
     * @param departmentName the name of the department
     * @return an Optional containing the DepartmentRequestDTO if found, otherwise empty
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
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

    /**
     * Fetches the average salary for a department.
     *
     * @param departmentName the name of the department
     * @return the average salary as a BigDecimal
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public BigDecimal fetchAverageSalary(String departmentName) {
        return this.lectorRepository.fetchAverageSalaryByDepartmentName(departmentName);
    }

    /**
     * Fetches the count of assistants in a department.
     *
     * @param departmentName the name of the department
     * @return the count of assistants
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public Integer fetchAssistantsCount(String departmentName) {
        return countLectorsByDegreeAndDepartment(departmentName, Degree.ASSISTANT);
    }

    /**
     * Fetches the count of associate professors in a department.
     *
     * @param departmentName the name of the department
     * @return the count of associate professors
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
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

    /**
     * Fetches the count of professors in a department.
     *
     * @param departmentName the name of the department
     * @return the count of professors
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
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

    /**
     * Fetches the head of a department by its name.
     *
     * @param departmentName the name of the department
     * @return the name of the head of the department
     * @throws NotFoundException if the department or head of department is not found
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
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

    /**
     * Fetches the count of employees in a department.
     *
     * @param departmentName the name of the department
     * @return the count of employees
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
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

    /**
     * Searches for lecturers by name containing the specified string.
     *
     * @param name the string to search for in the lecturer names
     * @return a list of lecturers whose names contain the specified string
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Loggable(
            logArguments = true,
            logReturnValue = true,
            logException = true
    )
    public List<Lector> searchLecturersByNameContaining(String name) {
        return this.lectorRepository.findByNameContaining(name);
    }

    /**
     * Searches for lecturers by name containing the specified string and degree.
     *
     * @param departmentName the string to search for in the lecturer names
     * @param degree the degree to search for
     * @return a list of lecturers whose names contain the specified string and have the specified degree
     */
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

    /**
     * Counts the number of lecturers with the specified degree in the specified department.
     *
     * @param departmentName the name of the department
     * @param degree the degree to search for
     * @return the count of lecturers with the specified degree
     */
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
