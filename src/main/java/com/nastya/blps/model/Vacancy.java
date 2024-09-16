package com.nastya.blps.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vacancies")
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    // Default constructor
    public Vacancy() {}

    // Constructor with fields
    public Vacancy(String title, String description, BigDecimal salary, Employer employer) {
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.employer = employer;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Vacancy{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", salary=" + salary +
                ", employerId=" + (employer != null ? employer.getId() : null) +
                '}';
    }
}