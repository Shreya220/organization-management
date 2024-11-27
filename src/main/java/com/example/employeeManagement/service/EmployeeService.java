package com.example.employeeManagement.service;

import com.example.employeeManagement.EmployeeDetailsDTO;
import com.example.employeeManagement.model.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    // Fetch all employees (basic info: name, id, designation)
    List<Map<String, Object>> findAll();

    // Fetch organization chart for a single employee
    Map<String, Object> getOrganizationChart(int employeeId);

    // Add employee from EmployeeDetailsDTO
    Employee save(EmployeeDetailsDTO employeeDTO);

    // Update employee from EmployeeDetailsDTO
    Employee updateEmployee(int employeeId, EmployeeDetailsDTO employeeDTO);

    // Delete employee
    void deleteEmployee(int employeeId);

    // Find employee by ID
    Employee findById(int employeeId);

    // Check if the employee is a Director
    boolean isDirector(int employeeId);

    // Check if an employee has direct reports
    boolean hasDirectReports(Employee employee);

    // Check if the reporting structure is valid
//    boolean isInvalidReportingStructure(Employee employee);

    // Check if there is already a Director in the organization
//    boolean isDirectorExists();

    // Reassign direct reports when a manager changes
    void moveDirectReports(int employeeId, Employee newManager);
}
