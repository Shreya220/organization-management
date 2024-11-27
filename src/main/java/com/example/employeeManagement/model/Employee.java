package com.example.employeeManagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private int employeeId;

    @Column(name = "name")
    private String name;

    // Many employees can share the same level (e.g., Manager, Lead)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "level_id", nullable = false) // Make level_id non-nullable
    private Level level;

    // Self-relationship for manager-employee structure
    @JsonBackReference
//    @ManyToOne
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    // List of direct reports (employees reporting to this employee)
    @JsonManagedReference
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL) // Added cascade for direct reports
    private List<Employee> directReports;

    // Constructors
    public Employee() {
        // Needed by JPA
    }

    public Employee(String name, Level level) {
        this.name = name;
        this.level = level;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public List<Employee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", level=" + (level != null ? level.getDesignation() : "None") +
                ", manager=" + (manager != null ? manager.getName() : "None") +
                '}';
    }
}

