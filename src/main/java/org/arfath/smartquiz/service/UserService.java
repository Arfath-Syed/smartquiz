package org.arfath.smartquiz.service;

import org.arfath.smartquiz.Model.UserEntity;
import org.arfath.smartquiz.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public  List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public UserEntity register(UserEntity user) {
        System.out.println("Received user: " + user);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }
}
