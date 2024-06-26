package com.loababa.api.auth.domain;

import com.loababa.api.auth.domain.impl.JWTManager;
import com.loababa.api.auth.domain.impl.OAuthClientProvider;
import com.loababa.api.auth.domain.impl.OAuthUserManager;
import com.loababa.api.auth.domain.impl.model.AuthToken;
import com.loababa.api.auth.domain.impl.model.OAuthCredential;
import com.loababa.api.auth.domain.impl.model.OAuthUser;
import com.loababa.api.auth.domain.impl.repository.RefreshTokenWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthClientProvider oAuthClientProvider;
    private final OAuthUserManager oAuthUserManager;
    private final JWTManager jwtManager;
    private final RefreshTokenWriter refreshTokenWriter;

    public AuthToken authenticate(OAuthCredential oAuthCredential) {  // todo: OAuth 표준 학습하고 표준에 맞추기
        var oAuthPlatform = oAuthCredential.platform();
        var oAuthClient = oAuthClientProvider.getOAuthClient(oAuthPlatform);

        var oAuthToken = oAuthClient.fetchOAuthToken(oAuthCredential.code());
        var oAuthUserInfo = oAuthClient.fetchUserOAuthInfo(oAuthToken.accessToken());
        oAuthClient.invalidateOAuthToken(oAuthToken.accessToken());

        OAuthUser oAuthUser = new OAuthUser(oAuthPlatform, oAuthUserInfo.id());
        oAuthUserManager.saveOAuthUserIfNotExists(oAuthUser);

        AuthToken authToken = jwtManager.generate();
        refreshTokenWriter.save(authToken.refreshToken());
        return authToken;
    }

}
