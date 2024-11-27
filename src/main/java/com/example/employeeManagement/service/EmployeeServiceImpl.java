package com.example.employeeManagement.service;

import com.example.employeeManagement.EmployeeDetailsDTO;
import com.example.employeeManagement.model.Level;
import com.example.employeeManagement.repo.EmployeeRepository;
import com.example.employeeManagement.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private com.example.employeeManagement.repo.LevelRepository levelRepository;

    // Fetch all employees (basic info: name, id, designation)
    @Override
    public List<Map<String, Object>> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapBasicEmployeeInfo)
                .collect(Collectors.toList());
    }

    // Fetch organization chart for a single employee
    @Override
    public Map<String, Object> getOrganizationChart(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Map<String, Object> orgChart = new LinkedHashMap<>();

        // Add Employee Info first
        orgChart.put("employeeInfo", mapBasicEmployeeInfo(employee));

        // Fetch Manager Info
        Employee manager = employee.getManager();
        if (manager != null) {
            orgChart.put("managerInfo", mapBasicEmployeeInfo(manager));
        } else {
            orgChart.put("managerInfo", null);
        }

        // Fetch Colleague Info (excluding the employee itself)
        List<Map<String, Object>> colleagues = employeeRepository.findByManager(employee.getManager())
                .stream()
                .filter(e -> e.getEmployeeId() != employeeId)
                .sorted(Comparator.comparing(Employee::getName))
                .map(this::mapBasicEmployeeInfo)
                .collect(Collectors.toList());
        orgChart.put("colleagueInfo", colleagues);

        // Fetch Employees Reporting to this Employee (Direct Reports)
        List<Map<String, Object>> directReports = employeeRepository.findByManager(employee)
                .stream()
                .sorted(Comparator.comparing(Employee::getName))
                .map(this::mapBasicEmployeeInfo)
                .collect(Collectors.toList());
        orgChart.put("directReports", directReports);

        return orgChart;
    }

    // Helper method to map basic employee information
    private Map<String, Object> mapBasicEmployeeInfo(Employee employee) {
        Map<String, Object> employeeInfo = new HashMap<>();
        employeeInfo.put("employeeId", employee.getEmployeeId());
        employeeInfo.put("name", employee.getName());
        employeeInfo.put("designation", employee.getLevel().getDesignation());
        return employeeInfo;
    }

    // Add employee from EmployeeDTO
    @Override
    public Employee save(EmployeeDetailsDTO employeeDTO) {
        // Ensure the employee's level is provided
        if (employeeDTO.getLevel() == null) {
            throw new RuntimeException("Level cannot be null.");
        }

        // Fetch the employee's level from the database
        Level level = levelRepository.findById(employeeDTO.getLevel().getId())
                .orElseThrow(() -> new RuntimeException("Level not found"));

        // Create new Employee object
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setLevel(level);

        // Set the manager if provided
        if (employeeDTO.getManager() != null) {
            Employee manager = employeeRepository.findById(employeeDTO.getManager().getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            if (manager.getLevel().getId() >= level.getId()) {
                throw new RuntimeException("Manager cannot be at the same or lower level than the employee.");
            }
            employee.setManager(manager);
        }

        // Save the employee
        return employeeRepository.save(employee);
    }

    // Update employee from EmployeeDTO
    @Override
    public Employee updateEmployee(int employeeId, EmployeeDetailsDTO employeeDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Update employee fields
        employee.setName(employeeDTO.getName());

        // Update level
        if (employeeDTO.getLevel() != null) {
            Level level = levelRepository.findById(employeeDTO.getLevel().getId())
                    .orElseThrow(() -> new RuntimeException("Level not found"));
            employee.setLevel(level);
        }

        // Update manager if provided
        if (employeeDTO.getManager() != null) {
            Level level = levelRepository.findById(employeeDTO.getLevel().getId())
                    .orElseThrow(() -> new RuntimeException("Level not found"));
            Employee manager = employeeRepository.findById(employeeDTO.getManager().getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            if (manager.getLevel().getId() >= level.getId()) {
                throw new RuntimeException("Manager cannot be at the same or lower level than the employee.");
            }
            employee.setManager(manager);
        }else {
            employee.setManager(null); // If no manager is provided, set to null
        }

        return employeeRepository.save(employee);
    }


    // Delete employee
    @Override
    public void deleteEmployee(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Employee manager = employee.getManager();

        // Reassign direct reports to the employee's manager (or set to null if no manager)
        employee.getDirectReports().forEach(report -> report.setManager(manager));
        employeeRepository.saveAll(employee.getDirectReports());

        employeeRepository.delete(employee);
    }

    // Reassign direct reports when a manager changes
    @Override
    public void moveDirectReports(int employeeId, Employee newManager) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        existingEmployee.getDirectReports().forEach(report -> report.setManager(newManager));
        employeeRepository.saveAll(existingEmployee.getDirectReports());
    }

    @Override
    public Employee findById(int employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
    }

    @Override
    public boolean isDirector(int employeeId) {
        Employee employee = findById(employeeId);
        return "Director".equalsIgnoreCase(employee.getLevel().getDesignation());
    }

    @Override
    public boolean hasDirectReports(Employee employee) {
        List<Employee> directReports = employeeRepository.findByManager(employee);
        return directReports != null && !directReports.isEmpty();
    }

}
