package com.hipradeep.authservice.repository;

import com.hipradeep.authservice.entity.RefreshToken;
import com.hipradeep.authservice.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {
    Optional<RefreshToken> findByUsername(String username);
    Optional<RefreshToken> findByRefreshToken(String username);

}
