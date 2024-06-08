package com.loababa.api.auth.domain.impl;

import com.loababa.api.auth.domain.impl.model.RefreshToken;
import com.loababa.api.auth.domain.impl.repository.RefreshTokenReader;
import com.loababa.api.auth.exception.InvalidTokenException;
import com.loababa.api.common.exception.ServerErrorInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.loababa.api.auth.exception.AuthClientErrorInfo.INVALID_CREDENTIALS;

@Component
@RequiredArgsConstructor
public class RefreshTokenValidator {

    public final RefreshTokenReader refreshTokenReader;

    public void validate(RefreshToken refreshToken) {
        if (!refreshTokenReader.existRefreshToken(refreshToken)) {
            throw new InvalidTokenException(
                    INVALID_CREDENTIALS,
                    new ServerErrorInfo(null, "존재하지 않는 RefreshToken 입니다. RefreshToken: " + refreshToken)
            );
        }
    }

}
