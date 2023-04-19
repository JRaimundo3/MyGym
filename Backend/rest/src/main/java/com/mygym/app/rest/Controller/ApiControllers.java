package com.mygym.app.rest.Controller;

import com.mygym.app.rest.Models.Login;
import com.mygym.app.rest.Models.Token;
import com.mygym.app.rest.Models.User;
import com.mygym.app.rest.Repo.TokenRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.digest.DigestUtils;
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
    @Autowired
    private TokenRepo tokenRepo;
    @GetMapping(value = "/")
    public String getPage(){
        return "MyGym!";
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login loginRequest) {
        Optional<User> optionalUser = userRepo.findByEmail(loginRequest.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (DigestUtils.sha512Hex(loginRequest.getPassword()).equals(user.getPassword())) {
                String token = Jwts.builder()
                        .setSubject(user.getEmail())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                        .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256))
                        .compact();

                Token tokenObj = new Token();
                tokenObj.setEmail(user.getEmail());
                tokenObj.setToken(token);
                tokenRepo.save(tokenObj);

                return ResponseEntity.ok(token);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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

    @GetMapping(value = "/getUsers")
    public List<User> getUsers(){
        return userRepo.findAll();
    }
}
