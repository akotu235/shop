package io.github.akotu235.shop.service.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.akotu235.shop.service.user.projection.UserWriteModel;
import io.github.akotu235.shop.util.FormatUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    private String password;
    private String email;
    private boolean enabled;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserRole> roles = new HashSet<>();
    @JsonIgnore
    private LocalDateTime registrationDate;
    @JsonIgnore
    private LocalDateTime lastLoginDate;

    private Locale locale;

    public User() {

    }

    public User(UserWriteModel user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.enabled = false;
        this.roles.add(new UserRole(this, "USER"));
        this.locale = user.getLocale();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public String getFormattedRegistrationDate() {
        return FormatUtils.formatDate(registrationDate);
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public String getFormattedLastLoginDate() {
        return FormatUtils.formatDate(lastLoginDate);
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public boolean hasRole(String role) {
        return roles.stream().anyMatch(userRole -> userRole.getRoleName().equals(role));
    }

    public void setRole(String role) {
        roles.add(new UserRole(this, role));
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @PrePersist
    void prePersist() {
        registrationDate = LocalDateTime.now();
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User otherUser = (User) obj;
        return Objects.equals(email, otherUser.email);
    }

    @Override
    public String toString() {
        return username;
    }
}
