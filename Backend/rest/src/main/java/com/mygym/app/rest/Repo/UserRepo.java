package com.mygym.app.rest.Repo;

import com.mygym.app.rest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo  extends JpaRepository<User, Long> {
}
