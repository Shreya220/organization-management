package com.example.employeeManagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Level")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "S_no")
    private int Sno;

    @Column(name = "level_id")
    private int id; // Not a primary key, just an identifier


    @Column(name = "designation")
    private String designation;

    // Constructors
    public Level() {
    }

    public Level(int id, String designation) {
        this.id = id;
        this.designation = designation;
    }

    // Getters and Setters
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

    public int getSno() {
        return Sno;
    }

    public void setSno(int sno) {
        Sno = sno;
    }

    @Override
    public String toString() {
        return "Level{" +
                "id=" + id +
                ", designation='" + designation + '\'' +
                '}';
    }
}
