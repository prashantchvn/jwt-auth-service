package com.exampleAuth.gateway.Entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
@Builder
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String otp;
    private boolean isVerified;
}
