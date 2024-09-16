package com.nastya.blps.dto;

public class EmployerRegistrationDto {
    private String username;
    private String password;
    private String email;
    private String companyName;
    private String contactPerson;
    private String phoneNumber;

    // Default constructor
    public EmployerRegistrationDto() {}

    // Constructor with all fields
    public EmployerRegistrationDto(String companyName, String email, String password, String contactPerson, String phoneNumber) {
        this.companyName = companyName;
        this.email = email;
        this.password = password;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // New getter and setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
