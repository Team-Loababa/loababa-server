package com.loababa.api.auth.infra.client.kakao;

import com.loababa.api.auth.domain.impl.OAuthClient;
import com.loababa.api.auth.domain.impl.model.OAuthPlatform;
import com.loababa.api.auth.domain.impl.model.OAuthToken;
import com.loababa.api.auth.domain.impl.model.OAuthUserInfo;
import com.loababa.api.auth.exception.ExternalCommunicationException;
import com.loababa.api.common.exception.ServerErrorInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.text.MessageFormat;

import static com.loababa.api.auth.exception.AuthClientErrorInfo.KAKAO_COMMUNICATION_FAIL;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClient {

    private static final String BEARER_PREFIX = "Bearer ";
    public static final String COMMUNICATION_ERROR_SERVER_MESSAGE = """
            request header: {0}
            request body: {1}
            response header: {2}
            response body: {3}
            """;

    private final RestClient restClient;
    private final KakaoProperties kakaoProperties;

    @Override
    public OAuthToken fetchOAuthToken(String code) {
        MultiValueMap<String, String> kakaoTokenRequestBody = kakaoProperties.toKakaoTokenRequestBody(code);

        KakaoOAuthToken kakaoOAuthToken = restClient
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
                .body(kakaoTokenRequestBody)
                .retrieve()
                .onStatus(
                        httpStatusCode -> httpStatusCode.is4xxClientError() && httpStatusCode.is5xxServerError(),
                        getErrorHandler(kakaoTokenRequestBody)
                )
                .body(KakaoOAuthToken.class);

        if (kakaoOAuthToken == null) {
            throw new ExternalCommunicationException(
                    KAKAO_COMMUNICATION_FAIL,
                    new ServerErrorInfo(null, "카카오의 response body가 null 입니다.")
            );
        }

        return new OAuthToken(kakaoOAuthToken.accessToken());
    }

    @Override
    public OAuthUserInfo fetchUserOAuthInfo(String accessToken) {
        KakaoOAuthAccessTokenInfo kakaoOAuthAccessTokenInfo = restClient
                .get()
                .uri("https://kapi.kakao.com/v1/user/access_token_info")
                .header(AUTHORIZATION, BEARER_PREFIX + accessToken)
                .retrieve()
                .onStatus(
                        httpStatusCode -> httpStatusCode.is4xxClientError() && httpStatusCode.is5xxServerError(),
                        getErrorHandler(null)
                )
                .body(KakaoOAuthAccessTokenInfo.class);

        if (kakaoOAuthAccessTokenInfo == null) {
            throw new ExternalCommunicationException(
                    KAKAO_COMMUNICATION_FAIL,
                    new ServerErrorInfo(null, "카카오의 response body가 null 입니다.")
            );
        }

        return new OAuthUserInfo(kakaoOAuthAccessTokenInfo.id());
    }

    @Override
    public void invalidateOAuthToken(String accessToken) {
        restClient
                .post()
                .uri("https://kapi.kakao.com/v1/user/logout")
                .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
                .header(AUTHORIZATION, BEARER_PREFIX + accessToken)
                .retrieve()
                .onStatus(
                        httpStatusCode -> httpStatusCode.is4xxClientError() && httpStatusCode.is5xxServerError(),
                        getErrorHandler(null)
                );
    }

    @Override
    public OAuthPlatform getOAuthPlatform() {
        return OAuthPlatform.KAKAO;
    }

    private RestClient.ResponseSpec.ErrorHandler getErrorHandler(MultiValueMap<String, String> requestBody) {
        return (request, response) -> {
            throw new ExternalCommunicationException(
                    KAKAO_COMMUNICATION_FAIL,
                    new ServerErrorInfo(
                            null,
                            MessageFormat.format(
                                    COMMUNICATION_ERROR_SERVER_MESSAGE,
                                    request.getHeaders(), requestBody,
                                    response.getHeaders(), response.getBody()
                            )
                    )
            );
        };
    }
}
