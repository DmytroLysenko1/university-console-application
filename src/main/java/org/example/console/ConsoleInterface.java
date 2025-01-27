package org.example.console;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.service.DepartmentService;
import org.example.service.LectorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.stream.Collectors;


/**
 * ConsoleInterface is a command-line interface for interacting with university data.
 * It allows users to perform various operations such as fetching the head of a department,
 * displaying department statistics, calculating average salaries, counting employees,
 * and performing global searches.
 */
@Component
@RequiredArgsConstructor
public class ConsoleInterface implements CommandLineRunner {
    private final DepartmentService departmentService;
    private final LectorService lectorService;

    /**
     * Initializes the ConsoleInterface bean and prints the initialized services.
     */
    @PostConstruct
    public void init() {
        System.out.printf(
                "ConsoleInterface bean initialized with: %s, %s%n",
                this.departmentService,
                this.lectorService
        );
    }

    /**
     * Runs the console interface, prompting the user for commands until 'exit' is entered.
     *
     * @param args command-line arguments
     */
    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the University console interface!");
        while (true) {
            System.out.println("Please enter the command:");
            String command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }
            processCommand(command);
        }
    }

    /**
     * Processes the user input command and delegates to the appropriate handler method.
     *
     * @param input the user input command
     */
    void processCommand(String input) {
        String commandType = getCommandType(input);
        switch (commandType) {
            case "head":
                handleHeadOfDepartment(input);
                break;
            case "statistics":
                handleStatistics(input);
                break;
            case "averageSalary":
                handleAverageSalary(input);
                break;
            case "employeeCount":
                handleEmployeeCount(input);
                break;
            case "globalSearch":
                handleGlobalSearch(input);
                break;
            default:
                System.out.println("Unknown command. Please try again.");
        }
    }

    /**
     * Determines the type of command based on the user input.
     *
     * @param input the user input command
     * @return the command type
     */
    String getCommandType(String input) {
        switch (input.split(" ")[0].toLowerCase()) {
            case "who":
                if (input.startsWith("Who is head of department")) {
                    return "head";
                }
                break;
            case "show":
                if (input.endsWith("statistics.")) {
                    return "statistics";
                } else if (input.startsWith("Show the average salary for the department")) {
                    return "averageSalary";
                } else if (input.startsWith("Show count of employee for")) {
                    return "employeeCount";
                }
                break;
            case "global":
                if (input.startsWith("Global search by")) {
                    return "globalSearch";
                }
                break;
            default:
                return "unknown";
        }
        return "unknown";
    }

    /**
     * Handles the 'Who is head of department' command.
     *
     * @param input the user input command
     */
    void handleHeadOfDepartment(String input) {
        String departmentName = extractDepartmentName(input);
        System.out.println("Extracted department name: " + departmentName);
        String headOfDepartment = this.departmentService.fetchHeadOfDepartment(departmentName);
        System.out.printf(
                "Head of %s department is %s%n",
                departmentName,
                headOfDepartment
        );
    }

    /**
     * Handles the 'Show statistics' command.
     *
     * @param input the user input command
     */
    void handleStatistics(String input) {
        String departmentName = extractDepartmentName(input);
        int assistants = this.departmentService.fetchAssistantsCount(departmentName);
        int associateProfessors = this.departmentService.fetchAssociateProfessorsCount(departmentName);
        int professors = this.departmentService.fetchProfessorsCount(departmentName);
        System.out.printf(
                "assistants - %d%nassociate professors - %d%nprofessors - %d%n",
                assistants,
                associateProfessors,
                professors
        );
    }

    /**
     * Handles the 'Show the average salary for the department' command.
     *
     * @param input the user input command
     */
    void handleAverageSalary(String input) {
        String departmentName = extractDepartmentName(input);
        double averageSalary = this.departmentService.fetchAverageSalary(departmentName)
                .doubleValue();
        System.out.printf(
                "The average salary of %s is %.2f%n",
                departmentName,
                averageSalary
        );
    }

    /**
     * Handles the 'Show count of employee for' command.
     *
     * @param input the user input command
     */
    void handleEmployeeCount(String input) {
        String departmentName = extractDepartmentName(input);
        int employeeCount = this.departmentService.fetchEmployeeCount(departmentName);
        System.out.printf(
                "%d%n",
                employeeCount
        );
    }

    /**
     * Handles the 'Global search by' command.
     *
     * @param input the user input command
     */
    void handleGlobalSearch(String input) {
        String template = input.replace(
                "Global search by",
                ""
        ).trim();
        String result = this.lectorService.searchByNameContaining(template)
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        System.out.println(result);
    }

    /**
     * Extracts the department name from the user input command.
     *
     * @param input the user input command
     * @return the extracted department name
     */
    String extractDepartmentName(String input) {
        return input.replaceAll(
                "(?i)(Who is head of department|Show the average salary for the department|Show count of employee for|Show|statistics\\.)",
                ""
        ).trim();
    }
}