package com.growtivat.auth_service.service;

import com.growtivat.auth_service.model.RefreshToken;
import com.growtivat.auth_service.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class RefreshTokenServiceTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    public RefreshTokenServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByToken_ShouldReturnRefreshToken() {
        RefreshToken refreshToken = RefreshToken.builder()
                .token("abc")
                .username("john_doe")
                .build();
        when(refreshTokenRepository.findByToken("abc"))
                .thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> result = refreshTokenService.findByToken("abc");
        assertTrue(result.isPresent());
        assertEquals(refreshToken.getToken(), result.get().getToken());
    }

    @Test
    void deleteByUsername_ShouldDeleteRefreshToken() {
        doNothing().when(refreshTokenRepository).deleteByUsername("john_doe");
        refreshTokenService.deleteByUsername("user");
        verify(refreshTokenRepository, times(1)).deleteByUsername("user");
    }
}
