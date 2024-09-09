package com.hipradeep.authservice.service;

import com.hipradeep.authservice.entity.RefreshToken;

public interface RefreshTokenService {

    public RefreshToken generateRefreshToken(String username);
    public RefreshToken verifyRefreshToken(String refreshToken);
}
