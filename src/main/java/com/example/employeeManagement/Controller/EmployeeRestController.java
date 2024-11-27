package com.example.employeeManagement.Controller;

import com.example.employeeManagement.EmployeeDetailsDTO;
import com.example.employeeManagement.model.Employee;
import com.example.employeeManagement.model.Level;
import com.example.employeeManagement.repo.EmployeeRepository;
import com.example.employeeManagement.repo.LevelRepository;
import com.example.employeeManagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    // Get all employees
    @GetMapping("/employees")
    public List<Map<String, Object>> findAll() {
        return employeeService.findAll();
    }

    // Get organization chart for a specific employee
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Map<String, Object>> getOrganizationChart(@PathVariable String employeeId) {
        try {
            int id = Integer.parseInt(employeeId);
            Map<String, Object> orgChart = employeeService.getOrganizationChart(id);
            return ResponseEntity.ok(orgChart);
        } catch (NumberFormatException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Incorrect Employee ID: " + employeeId);
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Employee ID not found: " + employeeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/employees")
    public ResponseEntity<Map<String, Object>> addEmployee(@RequestBody EmployeeDetailsDTO employeeDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate required fields
            if (employeeDTO.getName() == null) {
                response.put("message", "Incomplete JSON: Missing name.");
                return ResponseEntity.badRequest().body(response);
            }

            if (employeeDTO.getLevel() == null) {
                response.put("message", "Incomplete JSON: Missing level.");
                return ResponseEntity.badRequest().body(response);
            } else if (employeeDTO.getLevel().getDesignation() == null) {
                response.put("message", "Incomplete JSON: Missing designation.");
                return ResponseEntity.badRequest().body(response);
            }

            if (employeeDTO.getManager() == null) {
                response.put("message", "Incomplete JSON: Missing manager.");
                return ResponseEntity.badRequest().body(response);
            }

            // Initialize Employee object
            Employee savedEmployee = new Employee();
            savedEmployee.setName(employeeDTO.getName());

            // Fetch Level from the database
            Level level = levelRepository.findById(employeeDTO.getLevel().getId())
                    .orElseThrow(() -> new RuntimeException("Level not found"));
            savedEmployee.setLevel(level);

            // Fetch Manager if provided in the DTO (assuming it's within a ManagerDTO or manager object)
            if (employeeDTO.getManager() != null) {  // Check if manager object exists in DTO
                int managerId = employeeDTO.getManager().getEmployeeId();  // Assuming the manager object has an `id` field
                Employee manager = employeeRepository.findById(managerId)
                        .orElseThrow(() -> new RuntimeException("Manager not found"));
                savedEmployee.setManager(manager);
            }

            // Save the employee
            savedEmployee = employeeService.save(employeeDTO);
            response.put("message", "Employee added successfully.");
            response.put("employee", savedEmployee);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (HttpMessageNotReadableException e) {
            response.put("message", "Incomplete JSON: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("message", "Error saving employee: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable String employeeId, @RequestBody EmployeeDetailsDTO employeeDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (employeeDTO.getName() == null) {
                response.put("message", "Incomplete JSON: Missing name.");
                return ResponseEntity.badRequest().body(response);
            }

            if (employeeDTO.getLevel() == null || employeeDTO.getLevel().getId() == 0) {
                response.put("message", "Incomplete JSON: Missing level ID.");
                return ResponseEntity.badRequest().body(response);
            }

            // Fetch existing employee to update
            Employee existingEmployee = employeeRepository.findById(Integer.parseInt(employeeId))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found: " + employeeId));

            existingEmployee.setName(employeeDTO.getName());

            // Fetch Level from the database using the provided ID
            Level level = levelRepository.findById(employeeDTO.getLevel().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Level not found: " + employeeDTO.getLevel().getId()));

            existingEmployee.setLevel(level);

            // Fetch Manager if managerId is provided
            if (employeeDTO.getManager() != null && employeeDTO.getManager().getEmployeeId() != 0) {
                Employee manager = employeeRepository.findById(employeeDTO.getManager().getEmployeeId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found: " + employeeDTO.getManager().getEmployeeId()));
                existingEmployee.setManager(manager);
            }

            // Save the updated employee using the correct method signature
            Employee updatedEmployee = employeeService.updateEmployee(existingEmployee.getEmployeeId(), employeeDTO); // Pass employeeId and DTO
            response.put("message", "Employee updated successfully.");
            response.put("employee", updatedEmployee);
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            response.put("message", "Incorrect Employee ID: " + employeeId);
            return ResponseEntity.badRequest().body(response);
        } catch (ResponseStatusException e) {
            // Catch the 404 errors or other HTTP status exceptions
            response.put("message", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(response);
        } catch (RuntimeException e) {
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Delete an employee
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable String employeeId) {
        Map<String, Object> response = new HashMap<>();
        try {
            int id = Integer.parseInt(employeeId);

            // Prevent deleting Director if they have direct reports
            if (employeeService.isDirector(id) && employeeService.hasDirectReports(employeeService.findById(id))) {
                response.put("message", "Director cannot be removed unless they have no direct reports.");
                return ResponseEntity.badRequest().body(response);
            }

            employeeService.deleteEmployee(id);
            response.put("message", "Employee deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            response.put("message", "Incorrect Employee ID: " + employeeId);
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

