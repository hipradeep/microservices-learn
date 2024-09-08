package com.hipradeep.authservice.service.impl;

import com.hipradeep.authservice.config.CustomUserDetails;
import com.hipradeep.authservice.dto.UserCredentialDto;
import com.hipradeep.authservice.entity.UserCredential;
import com.hipradeep.authservice.repository.UserCredentialRepository;
import com.hipradeep.authservice.service.UserCredentialService;
import com.hipradeep.authservice.utils.DtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialServiceImpl implements UserCredentialService {

    @Autowired
    private UserCredentialRepository repository;

    @Override
    public UserCredentialDto getUserCredential(String username) {
        UserCredential credential = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + username));
        return DtoUtils.copyProperties(credential, UserCredentialDto.class);

    }
}
