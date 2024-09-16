package com.nastya.blps.service;

import com.nastya.blps.dto.VacancyDto;
import com.nastya.blps.model.Employer;
import com.nastya.blps.model.Vacancy;
import com.nastya.blps.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class VacancyService {

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private EmployerService employerService;

    public VacancyDto createVacancy(String title, String description, BigDecimal salary, String username) {
        Employer employer = employerService.getEmployerByUsername(username);
        Vacancy vacancy = new Vacancy(title, description, salary, employer);
        return convertToDto(vacancyRepository.save(vacancy));
    }

    public List<VacancyDto> getAllVacancies() {
        return vacancyRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public VacancyDto getVacancyById(Long id) {
        return convertToDto(vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found")));
    }

    public VacancyDto updateVacancy(Long id, String title, String description, BigDecimal salary, String username) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
        if (!vacancy.getEmployer().getUsername().equals(username)) {
            throw new RuntimeException("You don't have permission to update this vacancy");
        }
        vacancy.setTitle(title);
        vacancy.setDescription(description);
        vacancy.setSalary(salary);
        return convertToDto(vacancyRepository.save(vacancy));
    }

    public void deleteVacancy(Long id, String username) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
        if (!vacancy.getEmployer().getUsername().equals(username)) {
            throw new RuntimeException("You don't have permission to delete this vacancy");
        }
        vacancyRepository.delete(vacancy);
    }

    private VacancyDto convertToDto(Vacancy vacancy) {
        VacancyDto dto = new VacancyDto();
        dto.setId(vacancy.getId());
        dto.setTitle(vacancy.getTitle());
        dto.setDescription(vacancy.getDescription());
        dto.setSalary(vacancy.getSalary());
        dto.setEmployerId(vacancy.getEmployer() != null ? vacancy.getEmployer().getId() : null);
        return dto;
    }

}
