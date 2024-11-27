package com.example.employeeManagement;

public class EmployeeDetailsDTO {
    private String name;
    private LevelDTO level;  // Nested level DTO with id and designation
    private ManagerDTO manager;  // Nested manager DTO with employeeId

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LevelDTO getLevel() {
        return level;
    }

    public void setLevel(LevelDTO level) {
        this.level = level;
    }

    public ManagerDTO getManager() {
        return manager;
    }

    public void setManager(ManagerDTO manager) {
        this.manager = manager;
    }

    // Nested DTO classes for Level and Manager
    public static class LevelDTO {
        private int id;
        private String designation;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }
    }

    public static class ManagerDTO {
        private int employeeId;

        public int getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
        }
    }
}
