package com.hipradeep.user.services.services.impl;

import com.hipradeep.user.services.entities.User;
import com.hipradeep.user.services.exceptions.ResourceNotFoundException;
import com.hipradeep.user.services.repositories.UserRepository;
import com.hipradeep.user.services.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return this.userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User with given Id: "+userId+" not found on server !!"));
    }

    @Override
    public User updateUser(String userId, User user) {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }
}
