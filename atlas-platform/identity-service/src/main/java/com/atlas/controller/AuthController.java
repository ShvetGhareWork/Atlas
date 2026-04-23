package com.atlas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atlas.model.User;
import com.atlas.dto.AuthRequest;
import com.atlas.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String addNewUser(@RequestBody User user){
        return authService.saveUser(user);
    }

    @PostMapping("/login")
public String getToken(@RequestBody AuthRequest request) {
    // 1. This uses your CustomUserDetailsService behind the scenes to check the DB
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );

    // 2. If the password matches the BCrypt hash in the DB, generate the token
    if (authenticate.isAuthenticated()) {
        return authService.generateToken(request.getUsername());
    } else {
        throw new RuntimeException("Invalid access - check your credentials");
    }
}
}
