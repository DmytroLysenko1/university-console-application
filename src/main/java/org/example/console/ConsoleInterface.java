package org.example.console;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.service.DepartmentService;
import org.example.service.LectorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.System.out;

@Component
@RequiredArgsConstructor
public class ConsoleInterface implements CommandLineRunner {
    private final DepartmentService departmentService;
    private final LectorService lectorService;

    @PostConstruct
    public void init() {
        System.out.println("ConsoleInterface bean initialized with: " + departmentService + ", " + lectorService);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        out.println("Welcome to the University console interface!");
        while (true){
            out.println("Please enter the command:");
            String command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("exit")){
                out.println("Goodbye!");
                break;
            }
            processCommand(command);
        }
    }

    private void processCommand(String input){
        if (input.startsWith("Who is head of department")) {
            String departmentName = extractDepartmentName(input);
            String headOfDepartment = this.departmentService.fetchHeadOfDepartment(departmentName);
            out.printf("Head of %s department is %s%n", departmentName, headOfDepartment);
        } else if (input.startsWith("Show") && input.endsWith("statistics.")) {
            String departmentName = extractDepartmentName(input);
            int assistants = this.departmentService.fetchAssistantsCount(departmentName);
            int associateProfessors = this.departmentService.fetchAssociateProfessorsCount(departmentName);
            int professors = this.departmentService.fetchProfessorsCount(departmentName);
            out.printf("assistants - %d%nassociate professors - %d%nprofessors - %d%n",
                    assistants, associateProfessors, professors);
        }else if (input.startsWith("Show the average salary for the department")) {
            String departmentName = extractDepartmentName(input);
            double averageSalary = this.departmentService.fetchAverageSalary(departmentName).doubleValue();
            out.printf("The average salary of %s is %.2f%n", departmentName, averageSalary);
        } else if (input.startsWith("Show count of employee for")) {
            String departmentName = extractDepartmentName(input);
            int employeeCount = this.departmentService.fetchEmployeeCount(departmentName);
            out.printf("%d%n", employeeCount);
        } else if (input.startsWith("Global search by")) {
            String template = input.replace("Global search by", "").trim();
            String result = this.lectorService.searchByNameContaining(template)
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            out.println(result);
        } else {
            out.println("Unknown command. Please try again.");
        }
    }

    private String extractDepartmentName(String input) {
        return input.replaceAll("(Who is head of department|Show|Show the average salary for the department|Show count of employee for|statistics.)", "").trim();
    }
}
