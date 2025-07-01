package org.arfath.smartquiz.service;

import org.arfath.smartquiz.Model.UserEntity;
import org.arfath.smartquiz.repository.UserRepository;
import org.arfath.smartquiz.security.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        ;
    }

    public String register(UserEntity user) {
        System.out.println("Received user: " + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User Register Successfully";
    }

    public String login(UserEntity user) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        if(authenticate.isAuthenticated()){
            return jwtUtil.generateToken(user.getUsername());
        }else {
            throw new RuntimeException("Invalid Credentials");
        }

    }
}
