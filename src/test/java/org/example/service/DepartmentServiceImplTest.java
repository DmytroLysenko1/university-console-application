package org.example.service;

import org.example.dto.depatrment.DepartmentRequestDTO;
import org.example.entity.Department;
import org.example.entity.Lector;
import org.example.enums.Degree;
import org.example.exceptionHandling.customExceptions.NotFoundException;
import org.example.repositrory.DepartmentRepository;
import org.example.repositrory.LectorRepository;
import org.example.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private LectorRepository lectorRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Department department;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Lector lector;

    @BeforeEach
    void setUp() {
        this.lector = new Lector();
        this.lector.setName("Alice Johnson");
        this.lector.setDegree(Degree.PROFESSOR);
        this.lector.setSalary(BigDecimal.valueOf(70000));
        this.lector.setIsHeadOfDepartment(true);
    }

    @Test
    void findByName_validName_departmentRequestDTO() {
        when(this.departmentRepository.findByDepartmentName("Computer Science"))
                .thenReturn(Optional.of(this.department));
        when(this.modelMapper.map(this.department, DepartmentRequestDTO.class))
                .thenReturn(new DepartmentRequestDTO());

        Optional<DepartmentRequestDTO> result =
                this.departmentService.findByName("Computer Science");

        assertTrue(result.isPresent());
        verify(this.departmentRepository, times(1))
                .findByDepartmentName("Computer Science");
        verify(this.modelMapper, times(1))
                .map(this.department, DepartmentRequestDTO.class);
    }

    @Test
    void findByName_departmentNotFound() {
        when(this.departmentRepository.findByDepartmentName("NonExistent"))
                .thenReturn(Optional.empty());

        Optional<DepartmentRequestDTO> result =
                this.departmentService.findByName("NonExistent");

        assertTrue(result.isEmpty());
        verify(this.departmentRepository, times(1))
                .findByDepartmentName("NonExistent");
    }

    @Test
    void fetchAverageSalary_validDepartmentName_averageSalary() {
        when(this.lectorRepository.fetchAverageSalaryByDepartmentName("Computer Science"))
                .thenReturn(BigDecimal.valueOf(70000));

        BigDecimal result = this.departmentService.fetchAverageSalary("Computer Science");

        assertEquals(BigDecimal.valueOf(70000), result);
        verify(this.lectorRepository, times(1))
                .fetchAverageSalaryByDepartmentName("Computer Science");
    }

    @Test
    void fetchAverageSalary_departmentNotFound() {
        when(this.lectorRepository.fetchAverageSalaryByDepartmentName("NonExistent"))
                .thenThrow(new NotFoundException("Department not found", "NonExistent"));

        assertThrows(NotFoundException.class,
                () -> this.departmentService.fetchAverageSalary("NonExistent"));
        verify(this.lectorRepository, times(1))
                .fetchAverageSalaryByDepartmentName("NonExistent");
    }

    @Test
    void fetchAssistantsCount_validDepartmentName_assistantsCount() {
        when(this.departmentRepository.findByDepartmentName("Computer Science"))
                .thenReturn(Optional.of(this.department));
        when(this.department.getEmployees()).thenReturn(Set.of(this.lector));

        Integer result = this.departmentService.fetchAssistantsCount("Computer Science");

        assertEquals(0, result);
        verify(this.departmentRepository, times(1))
                .findByDepartmentName("Computer Science");
        verify(this.department, times(1)).getEmployees();
    }

    @Test
    void fetchAssociateProfessorsCount_validDepartmentName_associateProfessorsCount() {
        when(this.departmentRepository.findByDepartmentName("Computer Science"))
                .thenReturn(Optional.of(this.department));
        when(this.department.getEmployees()).thenReturn(Set.of(this.lector));

        Integer result = this.departmentService.fetchAssociateProfessorsCount("Computer Science");

        assertEquals(0, result);
        verify(this.departmentRepository, times(1))
                .findByDepartmentName("Computer Science");
        verify(this.department, times(1)).getEmployees();
    }

    @Test
    void fetchProfessorsCount_validDepartmentName_professorsCount() {
        when(this.departmentRepository.findByDepartmentName("Computer Science"))
                .thenReturn(Optional.of(this.department));
        when(this.department.getEmployees()).thenReturn(Set.of(this.lector));

        Integer result = this.departmentService.fetchProfessorsCount("Computer Science");

        assertEquals(1, result);
        verify(this.departmentRepository, times(1))
                .findByDepartmentName("Computer Science");
        verify(this.department, times(1))
                .getEmployees();
    }

    @Test
    void fetchHeadOfDepartment_validDepartmentName_headOfDepartmentName() {
        when(this.departmentRepository.findByDepartmentName("Computer Science"))
                .thenReturn(Optional.of(this.department));
        when(this.department.getHeadOfDepartment())
                .thenReturn(this.lector);

        String result = this.departmentService.fetchHeadOfDepartment("Computer Science");

        assertEquals("Alice Johnson", result);
        verify(this.departmentRepository, times(1))
                .findByDepartmentName("Computer Science");
        verify(this.department, times(1))
                .getHeadOfDepartment();
    }

    @Test
    void fetchHeadOfDepartment_departmentNotFound() {
        when(this.departmentRepository.findByDepartmentName("NonExistent"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> this.departmentService.fetchHeadOfDepartment("NonExistent"));
        verify(this.departmentRepository, times(1))
                .findByDepartmentName("NonExistent");
    }

    @Test
    void fetchEmployeeCount_validDepartmentName_employeeCount() {
        when(this.departmentRepository.findByDepartmentName("Computer Science"))
                .thenReturn(Optional.of(this.department));
        when(this.department.getEmployees())
                .thenReturn(Set.of(this.lector));

        Integer result = this.departmentService.fetchEmployeeCount("Computer Science");

        assertEquals(1, result);
        verify(this.departmentRepository, times(1))
                .findByDepartmentName("Computer Science");
        verify(this.department, times(1))
                .getEmployees();
    }

    @Test
    void fetchEmployeeCount_departmentNotFound() {
        when(this.departmentRepository.findByDepartmentName("NonExistent"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> this.departmentService.fetchEmployeeCount("NonExistent"));
        verify(this.departmentRepository, times(1))
                .findByDepartmentName("NonExistent");
    }

    @Test
    void searchLecturersByNameContaining_validName_lectorsList() {
        when(this.lectorRepository.findByNameContaining("Alice"))
                .thenReturn(List.of(this.lector));

        List<Lector> result = this.departmentService.searchLecturersByNameContaining("Alice");

        assertEquals(1, result.size());
        assertEquals("Alice Johnson", result.getFirst().getName());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("Alice");
    }

    @Test
    void searchLecturersByNameContaining_substring_lectorsList() {
        when(this.lectorRepository.findByNameContaining("John"))
                .thenReturn(List.of(this.lector));

        List<Lector> result = this.departmentService.searchLecturersByNameContaining("John");

        assertEquals(1, result.size());
        assertEquals("Alice Johnson", result.getFirst().getName());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("John");
    }

    @Test
    void searchLecturersByNameContaining_singleCharacter_lectorsList() {
        when(this.lectorRepository.findByNameContaining("A"))
                .thenReturn(List.of(this.lector));

        List<Lector> result = this.departmentService.searchLecturersByNameContaining("A");

        assertEquals(1, result.size());
        assertEquals("Alice Johnson", result.getFirst().getName());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("A");
    }

    @Test
    void searchLecturersByNameContaining_caseInsensitive_lectorsList() {
        Lector lector2 = new Lector();
        lector2.setName("alice johnson");

        when(this.lectorRepository.findByNameContaining("alice"))
                .thenReturn(List.of(this.lector, lector2));

        List<Lector> result = this.departmentService.searchLecturersByNameContaining("alice");

        assertEquals(2, result.size());
        assertEquals("Alice Johnson", result.getFirst().getName());
        assertEquals("alice johnson", result.getFirst().getName());
        verify(this.lectorRepository, times(1))
                .findByNameContaining("alice");
    }
}