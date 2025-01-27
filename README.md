# University Console Interface Application

## Overview

The University Console Interface Application is a command-line tool designed to interact with university data. It allows users to perform various operations such as fetching the head of a department, displaying department statistics, calculating average salaries, counting employees, and performing global searches.  

## Features

- **Head of Department**: Retrieve the head of a specified department.
- **Department Statistics**: Display the number of assistants, associate professors, and professors in a department.
- **Average Salary**: Calculate and display the average salary for a department.
- **Employee Count**: Count the number of employees in a department.
- **Global Search**: Search for employees by name across all departments.

## Technologies Used

- **Java**: The primary programming language used for the application.
- **Spring Boot**: Framework used to create the application, providing dependency injection, aspect-oriented programming, and more.
- **AspectJ**: Used for aspect-oriented programming to handle logging.
- **Maven**: Build automation tool used for managing project dependencies and building the application.
- **JUnit 5**: Testing framework used for writing and running tests.
- **Mockito**: Framework used for mocking objects in unit tests.
- **SLF4J**: Simple Logging Facade for Java, used for logging within the application.
- **Lombok**: Library used to reduce boilerplate code by generating getters, setters, and other methods at compile time.
- **Jakarta Annotations**: Used for dependency injection and lifecycle management.

## Getting Started

To run the application, clone the repository and use Maven to build and run the project. The application will start a command-line interface where you can enter commands to interact with the university data.

### Steps to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/university-console-interface.git
   cd <repository-directory>
   mvn clean install
   mvn spring-boot:run
   
## Usage

Once the application is running, you can enter commands such as:

- **Who is head of department `<department_name>`**: Retrieve the head of the specified department. For example in database stores such departments like : Mathematics Department, Medicine Department, Criminal Law Department.
- **Show the average salary for the department `<department_name>`**: Display the average salary for the specified department.
- **Show count of employee for `<department_name>`**: Count the number of employees in the specified department.
- **Global search by `<template>`**: Search for employees across all departments using the provided template.
- **Type `exit`** to close the application.

**Note**: Department names must be written with the first letter in uppercase as mentioned above in the usage.
