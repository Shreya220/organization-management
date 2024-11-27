package com.example.employeeManagement.service;

import com.example.employeeManagement.model.Level;

import java.util.List;

public interface LevelService {
    List<Level> findAll();
    Level save(Level level);
    Level findByDesignation(String designation);
}
