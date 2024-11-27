package com.example.employeeManagement.repo;

import com.example.employeeManagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // Find employees by level id
    List<Employee> findByLevel_Id(int levelId);

    @Query("SELECT e FROM Employee e WHERE e.level.designation = :designation")
    List<Employee> findByLevelDesignation(String designation);
    // Find employees reporting to a specific manager
    List<Employee> findByManager(Employee manager);
}
