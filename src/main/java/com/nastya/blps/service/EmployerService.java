package com.nastya.blps.service;

import com.nastya.blps.model.Employer;
import com.nastya.blps.repository.EmployerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import io.jsonwebtoken.Claims;
import com.nastya.blps.dto.EmployerDto;
import com.nastya.blps.dto.EmployerRegistrationDto;
import com.nastya.blps.dto.EmployerPublicInfoDto;

@Service
public class EmployerService implements UserDetailsService {

    private final EmployerRepository employerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployerService(EmployerRepository employerRepository, PasswordEncoder passwordEncoder) {
        this.employerRepository = employerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public void registerEmployer(EmployerRegistrationDto registrationDto) {
        Employer employer = new Employer();
        employer.setUsername(registrationDto.getUsername());
        employer.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        employer.setEmail(registrationDto.getEmail());
        employer.setCompanyName(registrationDto.getCompanyName());
        employer.setContactPerson(registrationDto.getContactPerson());
        employer.setPhoneNumber(registrationDto.getPhoneNumber());

        employerRepository.save(employer);
    }

    public String loginEmployer(String username, String password) {
        Employer employer = employerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        
        if (passwordEncoder.matches(password, employer.getPassword())) {
            return generateJwtToken(employer);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    private String generateJwtToken(Employer employer) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(Long.toString(employer.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public Employer getEmployerByEmail(String email) {
        return employerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
    }

    public Employer getEmployerByUsername(String username) {
        return employerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getEmployerIdFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employer employer = employerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(employer.getUsername(), employer.getPassword(),
                new ArrayList<>());
    }

    public UserDetails loadUserById(Long id) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found with id: " + id));
        return new org.springframework.security.core.userdetails.User(employer.getUsername(), employer.getPassword(),
                new ArrayList<>());
    }

    public EmployerDto getCurrentEmployer(String username) {
        Employer employer = employerRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));
        return convertToDto(employer);
    }

    private EmployerDto convertToDto(Employer employer) {
        EmployerDto dto = new EmployerDto();
        dto.setId(employer.getId());
        dto.setUsername(employer.getUsername());
        dto.setCompanyName(employer.getCompanyName());
        dto.setEmail(employer.getEmail());
        dto.setContactPerson(employer.getContactPerson());
        dto.setPhoneNumber(employer.getPhoneNumber());
        return dto;
    }

    public EmployerPublicInfoDto getEmployerPublicInfo(Long id) {
        Employer employer = employerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employer not found"));
        return convertToPublicInfoDto(employer);
    }

    private EmployerPublicInfoDto convertToPublicInfoDto(Employer employer) {
        EmployerPublicInfoDto dto = new EmployerPublicInfoDto();
        dto.setId(employer.getId());
        dto.setCompanyName(employer.getCompanyName());
        dto.setContactPerson(employer.getContactPerson());
        dto.setPhoneNumber(employer.getPhoneNumber());
        return dto;
    }
}