package com.nastya.blps.controller;

import com.nastya.blps.dto.EmployerRegistrationDto;
import com.nastya.blps.dto.LoginDto;
import com.nastya.blps.dto.EmployerDto;
import com.nastya.blps.dto.EmployerPublicInfoDto;
import com.nastya.blps.service.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/employers")
public class EmployerController {

    private final EmployerService employerService;

    @Autowired
    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployer(@RequestBody EmployerRegistrationDto registrationDto) {
        employerService.registerEmployer(registrationDto);
        return ResponseEntity.ok("Employer registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginEmployer(@RequestBody LoginDto loginDto) {
        String token = employerService.loginEmployer(loginDto.getUsername(), loginDto.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> getCurrentEmployer(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String username = authentication.getName();
        EmployerDto employerDto = employerService.getCurrentEmployer(username);
        return ResponseEntity.ok(employerDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employer's public information by ID")
    public ResponseEntity<EmployerPublicInfoDto> getEmployerPublicInfo(@PathVariable Long id) {
        EmployerPublicInfoDto employerInfo = employerService.getEmployerPublicInfo(id);
        return ResponseEntity.ok(employerInfo);
    }
}