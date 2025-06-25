package com.growtivat.auth_service.repository;

import java.util.Optional;
import java.util.UUID;

import com.growtivat.auth_service.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    void deleteByToken(String token);

    Optional<RefreshToken> findByToken(String token);

    void deleteByUsername(String username);

    Optional<RefreshToken> findByUsernameAndDeviceId(String username, UUID deviceId);
}
