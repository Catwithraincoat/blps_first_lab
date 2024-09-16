package com.nastya.blps.controller;

import com.nastya.blps.model.Vacancy;
import com.nastya.blps.dto.VacancyDto;
import com.nastya.blps.security.JwtUtil;
import com.nastya.blps.service.VacancyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyController {

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    @Operation(summary = "Create a new vacancy", security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> createVacancy(@RequestBody CreateVacancyRequest request,
                                           Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            VacancyDto vacancy = vacancyService.createVacancy(request.getTitle(), request.getDescription(),
                    request.getSalary(), username);
            return ResponseEntity.ok(vacancy);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<VacancyDto>> getAllVacancies() {
        return ResponseEntity.ok(vacancyService.getAllVacancies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVacancy(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(vacancyService.getVacancyById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a vacancy", security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> updateVacancy(@PathVariable Long id,
                                           @RequestBody UpdateVacancyRequest request,
                                           Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            VacancyDto vacancy = vacancyService.updateVacancy(id, request.getTitle(), request.getDescription(),
                    request.getSalary(), username);
            return ResponseEntity.ok(vacancy);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vacancy", security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteVacancy(@PathVariable Long id,
                                           Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username= userDetails.getUsername();
            vacancyService.deleteVacancy(id, username);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Request classes
    static class CreateVacancyRequest {
        private String title;
        private String description;
        private BigDecimal salary;

        // getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getSalary() { return salary; }
        public void setSalary(BigDecimal salary) { this.salary = salary; }
    }

    static class UpdateVacancyRequest {
        private String title;
        private String description;
        private BigDecimal salary;

        // getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getSalary() { return salary; }
        public void setSalary(BigDecimal salary) { this.salary = salary; }
    }
}
