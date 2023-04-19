package com.mygym.app.rest.Repo;

import com.mygym.app.rest.Models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Long> {
    Optional<Token> findByEmail(String email);
}

