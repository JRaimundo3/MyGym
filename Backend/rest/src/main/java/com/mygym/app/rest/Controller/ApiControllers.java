package com.mygym.app.rest.Controller;

import com.mygym.app.rest.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import com.mygym.app.rest.Repo.UserRepo;
@RestController
public class ApiControllers {
    @Autowired
    private UserRepo userRepo;
    @GetMapping(value = "/")
    public String getPage(){
        return "MyGym!";
    }
    @GetMapping(value = "/getUsers")
    public List<User> getUsers(){
        return userRepo.findAll();
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> doRegister(@RequestBody User user) {
        String userEmail = user.getEmail();
        if (userEmail != null && userRepo.findByEmail(userEmail).isEmpty()) {
            userRepo.save(user);
            return ResponseEntity.ok("User with email " + userEmail + " successfully registered");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error registering user with email " + userEmail);
        }
    }

}
