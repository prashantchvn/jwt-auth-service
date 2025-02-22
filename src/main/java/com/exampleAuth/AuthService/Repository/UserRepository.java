package com.exampleAuth.AuthService.Repository;

import com.exampleAuth.AuthService.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
