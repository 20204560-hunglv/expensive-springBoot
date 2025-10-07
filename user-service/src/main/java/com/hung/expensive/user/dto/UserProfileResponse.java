package com.hung.expensive.user.dto;

import java.time.LocalDateTime;

/**
 * Why: User Profile Response DTO cho API responses
 * Design decision: Separate response DTO để control output format
 * Business requirement: Return user profile information to clients
 */
public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Why: Default constructor cho Jackson serialization
    public UserProfileResponse() {}

    // Why: Constructor với required fields
    public UserProfileResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Why: Builder pattern cho flexible object creation
    public static UserProfileResponseBuilder builder() {
        return new UserProfileResponseBuilder();
    }

    public static class UserProfileResponseBuilder {
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String address;
        private String city;
        private String country;
        private String postalCode;
        private Boolean enabled;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public UserProfileResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserProfileResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserProfileResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserProfileResponseBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserProfileResponseBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserProfileResponseBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserProfileResponseBuilder address(String address) {
            this.address = address;
            return this;
        }

        public UserProfileResponseBuilder city(String city) {
            this.city = city;
            return this;
        }

        public UserProfileResponseBuilder country(String country) {
            this.country = country;
            return this;
        }

        public UserProfileResponseBuilder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public UserProfileResponseBuilder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserProfileResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserProfileResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserProfileResponse build() {
            UserProfileResponse response = new UserProfileResponse();
            response.setId(this.id);
            response.setUsername(this.username);
            response.setEmail(this.email);
            response.setFirstName(this.firstName);
            response.setLastName(this.lastName);
            response.setPhoneNumber(this.phoneNumber);
            response.setAddress(this.address);
            response.setCity(this.city);
            response.setCountry(this.country);
            response.setPostalCode(this.postalCode);
            response.setEnabled(this.enabled);
            response.setCreatedAt(this.createdAt);
            response.setUpdatedAt(this.updatedAt);
            return response;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
