package com.hipradeep.user.services.services.impl;

import com.hipradeep.user.services.entities.UserProfile;
import com.hipradeep.user.services.repositories.UserProfileRepository;
import com.hipradeep.user.services.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;



    @Override
    public UserProfile uploadProfile(UserProfile user) {
        return this.userProfileRepository.save(user);
    }
}
