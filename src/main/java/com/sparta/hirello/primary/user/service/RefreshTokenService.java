package com.sparta.hirello.primary.user.service;

import com.sparta.hirello.primary.user.entity.RefreshToken;
import com.sparta.hirello.primary.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(String username, String refreshToken) {
        RefreshToken existToken = refreshTokenRepository.findByUsername(username).orElse(null);
        if (existToken == null) {
            refreshTokenRepository.save(new RefreshToken(username, refreshToken));
        } else {
            existToken.update(refreshToken);
        }
    }

}
