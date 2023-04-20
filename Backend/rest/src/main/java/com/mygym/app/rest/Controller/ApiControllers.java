package com.mygym.app.rest.Controller;

import com.mygym.app.rest.Models.*;
import com.mygym.app.rest.Models.Lesson;
import com.mygym.app.rest.Repo.LessonRepo;
import com.mygym.app.rest.Repo.TokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import com.mygym.app.rest.Repo.UserRepo;
@RestController
public class ApiControllers {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TokenRepo tokenRepo;
    @Autowired
    private LessonRepo lessonRepo;
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error authenticating user with email " + loginRequest.getEmail());
    }

    @GetMapping("/verifyToken")
    public ResponseEntity<String> verifyToken(@RequestParam String token) {
        Optional<Token> optionalToken = tokenRepo.findByToken(token);

        if (optionalToken.isPresent()) {
            Token dbToken = optionalToken.get();
            if (dbToken.getToken().equals(token) && dbToken.getCreatedAt().getTime() + 5 * 60 * 1000 >= System.currentTimeMillis()) {
                return ResponseEntity.ok("Token is valid");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
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
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @PostMapping("/class/schedule")
    public ResponseEntity<String> scheduleClass(@RequestParam String token, @RequestBody Lesson lesson) {

        ResponseEntity<String> response = verifyToken(token);
        if (response.getStatusCode() != HttpStatus.OK) {
            return response;
        }

        lessonRepo.save(lesson);
        return ResponseEntity.ok("Class created successfully");
    }
    @GetMapping("/class/list")
    public ResponseEntity<?> getClasses(@RequestParam String token) {
        ResponseEntity<String> tokenResponse = verifyToken(token);
        if (tokenResponse.getStatusCode() != HttpStatus.OK) {
            return tokenResponse;
        }

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        List<Lesson> upcomingLessons = lessonRepo.findAllUpcomingLessons(currentDate, currentTime);
        return ResponseEntity.ok(upcomingLessons);
    }

    @GetMapping("/getUser")
    public ResponseEntity<?>  getUser(@RequestParam String token) {
        Optional<Token> optionalToken = tokenRepo.findByToken(token);

        if (optionalToken.isPresent()) {
            Token dbToken = optionalToken.get();
            Optional<User> optionalUser = userRepo.findByEmail(dbToken.getEmail());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email " + dbToken.getEmail() + " not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
        }
    }

}
