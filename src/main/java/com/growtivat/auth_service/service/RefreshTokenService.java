package com.growtivat.auth_service.service;

import com.growtivat.auth_service.model.RefreshToken;
import com.growtivat.auth_service.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> findByUsernameAndDeviceId(String username, UUID deviceId) {
        return refreshTokenRepository.findByUsernameAndDeviceId(username, deviceId);
    }

    @Transactional
    public void deleteByToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}