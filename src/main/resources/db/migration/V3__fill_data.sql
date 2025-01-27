INSERT INTO lectors (id, name, degree, salary, is_head_of_department) VALUES
                                                                          (1, 'Alice Johnson', 'PROFESSOR', 70000.00, TRUE),
                                                                          (2, 'Bob Smith', 'ASSOCIATE_PROFESSOR', 60000.00, FALSE),
                                                                          (3, 'Charlie Brown', 'ASSISTANT', 50000.00, FALSE),
                                                                          (4, 'David White', 'PROFESSOR', 75000.00, TRUE),
                                                                          (5, 'Eve Black', 'ASSOCIATE_PROFESSOR', 62000.00, FALSE),
                                                                          (6, 'Frank Green', 'ASSISTANT', 51000.00, FALSE),
                                                                          (7, 'Grace Taylor', 'PROFESSOR', 80000.00, TRUE),
                                                                          (8, 'Hannah King', 'ASSOCIATE_PROFESSOR', 65000.00, FALSE),
                                                                          (9, 'Ian Scott', 'ASSISTANT', 52000.00, FALSE);

INSERT INTO departments (id, department_name, head_of_department_id) VALUES
                                                                         (1, 'Mathematics Department', 1),
                                                                         (2, 'Medicine Department', 4),
                                                                         (3, 'Criminal Law Department', 7);

INSERT INTO department_lector (department_id, lector_id) VALUES
                                                             (1, 1),
                                                             (1, 2),
                                                             (1, 3),
                                                             (1, 4),
                                                             (2, 1),
                                                             (2, 4),
                                                             (2, 5),
                                                             (3, 2),
                                                             (3, 6),
                                                             (3, 7),
                                                             (3, 8),
                                                             (2, 9);