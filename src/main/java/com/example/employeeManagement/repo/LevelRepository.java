package com.example.employeeManagement.repo;

import com.example.employeeManagement.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Integer> {
    Level findByDesignation(String designation);

}
