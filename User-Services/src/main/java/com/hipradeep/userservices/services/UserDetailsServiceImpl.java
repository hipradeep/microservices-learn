package com.hipradeep.userservices.services;


import com.hipradeep.userservices.entities.User;
import com.hipradeep.userservices.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).get();
        if (user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    //.roles(user.getRoles().toArray(new String[0]))//getting user details with user role,
                    //.roles("ADMIN") // Fixed role for all users
                    .build();
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}

