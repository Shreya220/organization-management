CREATE TABLE IF NOT EXISTS Level (
                                     S_no INT AUTO_INCREMENT PRIMARY KEY,
                                     level_id INT NOT NULL,
                                     designation VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS Employee (
                                        employee_id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255),
    manager_id INT,
    level_id INT,
    FOREIGN KEY (manager_id) REFERENCES Employee(employee_id),
    FOREIGN KEY (level_id) REFERENCES Level(S_no)
    );
