package org.example.library.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {

    @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
    private String name;

    @Email(message = "Email deve ser válido")
    @Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
    private String email;

    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

    @Size(max = 14, message = "Telefone deve ter no máximo 14 caracteres")
    private String phone;

    @Size(max = 500, message = "Bio deve ter no máximo 500 caracteres")
    private String bio;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
