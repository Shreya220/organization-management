package com.example.employeeManagement.service;

import com.example.employeeManagement.model.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelServiceImpl implements LevelService {
    @Autowired
    private com.example.employeeManagement.repo.LevelRepository levelRepository;

    // Fetch all levels
    public List<Level> findAll() {
        return levelRepository.findAll();
    }

    // Find a specific level by designation
    public Level findByDesignation(String designation) {
        return levelRepository.findByDesignation(designation);
    }

    // Save a new level
    public Level save(Level level) {
        return levelRepository.save(level);
    }


}
