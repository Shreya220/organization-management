-- Insert Levels (no need to specify S_no since it's auto-generated)
INSERT INTO Level (level_id, designation) VALUES (1, 'Director');
INSERT INTO Level (level_id, designation) VALUES (2, 'Manager');
INSERT INTO Level (level_id, designation) VALUES (3, 'Lead');
INSERT INTO Level (level_id, designation) VALUES (4, 'Developer');
INSERT INTO Level (level_id, designation) VALUES (4, 'DevOps');
INSERT INTO Level (level_id, designation) VALUES (4, 'QA');
INSERT INTO Level (level_id, designation) VALUES (5, 'Intern');

-- Insert Employees
INSERT INTO Employee (name, manager_id, level_id) VALUES ('Thor', NULL, 1);
INSERT INTO Employee (name, manager_id, level_id) VALUES ('IronMan', 1, 2);
INSERT INTO Employee (name, manager_id, level_id) VALUES ('Hulk', 1, 3);
INSERT INTO Employee (name, manager_id, level_id) VALUES ('CaptainAmerica', 1, 3);
INSERT INTO Employee (name, manager_id, level_id) VALUES ('War Machine', 2, 4);
INSERT INTO Employee (name, manager_id, level_id) VALUES ('SpiderMan', 2, 7);
INSERT INTO Employee (name, manager_id, level_id) VALUES ('Vision', 2, 5);
INSERT INTO Employee (name, manager_id, level_id) VALUES ('BlackWidow', 3, 6);
INSERT INTO Employee (name, manager_id, level_id) VALUES ('Falcon', 4, 7);
INSERT INTO Employee (name, manager_id, level_id) VALUES ('AntMan', 4, 3);
