package com.hipradeep.authservice.service.impl;

import com.hipradeep.authservice.entity.RefreshToken;
import com.hipradeep.authservice.entity.UserCredential;
import com.hipradeep.authservice.repository.RefreshTokenRepository;
import com.hipradeep.authservice.repository.UserCredentialRepository;
import com.hipradeep.authservice.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    public long refreshTokenTime  = 10*60*1000;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserCredentialRepository userCredentialRepository;


    @Override
    public RefreshToken generateRefreshToken(String username) {
        UserCredential user = userCredentialRepository.findByUsername(username).get();
        System.out.println("27 user name: " + user.getUsername());
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByUsername(username);
        RefreshToken refreshToken=null;
        if (refreshTokenOpt.isPresent()) {
             refreshToken = refreshTokenOpt.get();
        }
        if (refreshToken == null) {
            refreshToken = RefreshToken.builder().refreshToken(UUID.randomUUID().toString())
                    .expiredTime(Instant.now().plusMillis(refreshTokenTime))
                    .username(user.getUsername()).build();
        } else {
            refreshToken.setExpiredTime(Instant.now().plusMillis(refreshTokenTime));
        }

        refreshTokenRepository.save(refreshToken);
        //user.setRefreshToken(refreshToken);
        System.out.println(" refreshing token " + refreshToken);
        return refreshToken;

    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken) {

        RefreshToken refreshTokenOb = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh Token not found!"));

        if (refreshTokenOb.getExpiredTime().compareTo(Instant.now()) < 0) {
            throw new RuntimeException("Refresh Token Expired!!");
        }

        return refreshTokenOb;
    }

}
