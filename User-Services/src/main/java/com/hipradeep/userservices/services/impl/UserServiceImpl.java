package com.hipradeep.userservices.services.impl;

import com.hipradeep.userservices.entities.User;
import com.hipradeep.userservices.exceptions.ResourceNotFoundException;
import com.hipradeep.userservices.repositories.UserRepository;
import com.hipradeep.userservices.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User saveUser(User user) {
        //String randomUserId = UUID.randomUUID().toString();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(()->
                new ResourceNotFoundException("User with given Id: "+userName+" not found on server !!"));
    }

    @Override
    public User updateUser(String userId, User user) {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }
}
