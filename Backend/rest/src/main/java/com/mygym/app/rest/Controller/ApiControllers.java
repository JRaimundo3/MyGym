package com.mygym.app.rest.Controller;

import com.mygym.app.rest.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String doRegister(@RequestBody User user){
        userRepo.save(user);
        return "User saved with Success";
    }
}
