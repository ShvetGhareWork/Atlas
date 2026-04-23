package com.atlas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // CONVERTS THIS CLASS INTO A DB TABLE
@Table(name = "users")
@Data // GETTERS, SETTERS, TOSTRING
@AllArgsConstructor // CONSTRUCTOR WITH ALL FIELDS
@NoArgsConstructor // DEFAULT CONSTRUCTOR (GEN EMPTY OBJECTS)
public class User {

    @Id // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO INCREMENTED INTEGER
    private Integer id;
    private String username;
    private String email;
    private String password; // HASHED PASSWORD

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
