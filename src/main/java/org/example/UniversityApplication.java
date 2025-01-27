package org.example;

import lombok.RequiredArgsConstructor;
import org.example.console.ConsoleInterface;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class UniversityApplication implements CommandLineRunner {

    private final ConsoleInterface consoleInterface;

    public static void main(String[] args) {
        SpringApplication.run(UniversityApplication.class, args);
    }

    @Override
    public void run(String... args) {
        consoleInterface.run(args);
    }
}
