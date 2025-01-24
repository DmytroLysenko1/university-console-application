CREATE TABLE IF NOT EXISTS organizational_units (
    id BIGSERIAL PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS lectors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    degree VARCHAR(255),
    salary NUMERIC(15, 2),
    is_head_of_department BOOLEAN
);

CREATE TABLE IF NOT EXISTS departments (
    id BIGSERIAL PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL,
    head_of_department_id BIGINT UNIQUE,
    FOREIGN KEY (head_of_department_id) REFERENCES lectors(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS department_lector (
    department_id BIGINT NOT NULL,
    lector_id BIGINT NOT NULL,
    PRIMARY KEY (department_id, lector_id),
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE,
    FOREIGN KEY (lector_id) REFERENCES lectors(id) ON DELETE CASCADE
);

