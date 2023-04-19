package com.mygym.app.rest.Models;

import jakarta.persistence.*;
import org.apache.commons.codec.digest.DigestUtils;

import javax.validation.constraints.Email;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String phoneNumber;

    @Column
    private String gym;


    @Email(message = "E-mail inválido")
    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String role;



    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = DigestUtils.sha512Hex(password);
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
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
    public String getGym() {
        return gym;
    }
    public void setGym(String gym) {
        this.gym = gym;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
