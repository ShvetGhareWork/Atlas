package com.atlas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.atlas.dto.AuthRequest;
import com.atlas.model.User;
import com.atlas.repo.UserRepository;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String saveUser(User user){
        // HASH the password before saving. Never store plain text password..
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // SAVE TO DB
        repository.save(user);
        // RETURN THE TOKEN
        return "User registered successfully";
    }

    public String generateToken(AuthRequest request){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(authenticate.isAuthenticated()){
            return jwtService.generateToken(request.getUsername());
        }else{
            throw new RuntimeException("invalid access");
        }
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token){
        jwtService.validateToken(token);
    }
}
