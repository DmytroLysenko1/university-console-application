package org.example.console;

import org.example.service.DepartmentService;
import org.example.service.LectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ConsoleInterfaceTest {

    @Mock
    private DepartmentService departmentService;

    @Mock
    private LectorService lectorService;

    @InjectMocks
    private ConsoleInterface consoleInterface;

    @BeforeEach
    void setupDependencies_initialization_success() {
        this.consoleInterface = new ConsoleInterface(this.departmentService, this.lectorService);
    }

    @Test
    void handleHeadOfDepartment_validDepartmentName_correctOutput() {
        String input = "Who is head of department Medicine";
        when(this.departmentService.fetchHeadOfDepartment("Medicine"))
                .thenReturn("Dr. Smith");

        this.consoleInterface.handleHeadOfDepartment(input);

        verify(this.departmentService, times(1))
                .fetchHeadOfDepartment("Medicine");
    }

    @Test
    void handleStatistics_validDepartmentName_correctCounts() {
        String input = "Show Medicine statistics.";
        when(this.departmentService.fetchAssistantsCount("Medicine"))
                .thenReturn(10);
        when(this.departmentService.fetchAssociateProfessorsCount("Medicine"))
                .thenReturn(5);
        when(this.departmentService.fetchProfessorsCount("Medicine"))
                .thenReturn(3);

        this.consoleInterface.handleStatistics(input);

        verify(this.departmentService, times(1))
                .fetchAssistantsCount("Medicine");
        verify(this.departmentService, times(1))
                .fetchAssociateProfessorsCount("Medicine");
        verify(this.departmentService, times(1))
                .fetchProfessorsCount("Medicine");
    }

    @Test
    void handleAverageSalary_validDepartmentName_correctSalary() {
        String input = "Show the average salary for the department Medicine";
        when(this.departmentService.fetchAverageSalary("Medicine"))
                .thenReturn(BigDecimal.valueOf(5000.00));

        this.consoleInterface.handleAverageSalary(input);

        verify(this.departmentService, times(1))
                .fetchAverageSalary("Medicine");
    }

    @Test
    void handleEmployeeCount_validDepartmentName_correctCount() {
        String input = "Show count of employee for Medicine";
        when(this.departmentService.fetchEmployeeCount("Medicine"))
                .thenReturn(50);

        this.consoleInterface.handleEmployeeCount(input);

        verify(this.departmentService, times(1))
                .fetchEmployeeCount("Medicine");
    }

    @Test
    void handleGlobalSearch_validSearchQuery_correctResults() {
        String input = "Global search by John";
        when(this.lectorService.searchByNameContaining("John"))
                .thenReturn(List.of("John Doe", "Johnny Bravo"));

        this.consoleInterface.handleGlobalSearch(input);

        verify(this.lectorService, times(1))
                .searchByNameContaining("John");
    }

    @Test
    void processCommand_invalidCommand_showsUnknownMessage() {
        String input = "Invalid command";
        this.consoleInterface.processCommand(input);
    }

    @Test
    void extractDepartmentName_validInput_correctDepartmentName() {
        String input = "Who is head of department Biology";
        String departmentName = this.consoleInterface.extractDepartmentName(input);
        assertEquals("Biology", departmentName);
    }

    @Test
    void getCommandType_validCommand_correctCommandType() {
        assertEquals("head",
                this.consoleInterface.getCommandType("Who is head of department Medicine"));
        assertEquals("statistics",
                this.consoleInterface.getCommandType("Show Medicine statistics."));
        assertEquals("averageSalary",
                this.consoleInterface.getCommandType("Show the average salary for the department Medicine"));
        assertEquals("employeeCount",
                this.consoleInterface.getCommandType("Show count of employee for Medicine"));
        assertEquals("globalSearch",
                this.consoleInterface.getCommandType("Global search by John"));
        assertEquals("unknown",
                this.consoleInterface.getCommandType("Invalid command"));
    }

    @Test
    void run_validExitCommand_exitsGracefully() {
        String input = "exit\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        this.consoleInterface.run();
    }
}
