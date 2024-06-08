package com.loababa.api.auth.exception;

import com.loababa.api.common.exception.ClientErrorInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthClientErrorInfo implements ClientErrorInfo {
    INVALID_CREDENTIALS("잘못된 토큰 값입니다."),

    // KAKAO_OAUTH
    KAKAO_COMMUNICATION_FAIL("카카오 서버 연결에 실패헀습니다.");

    private final String clientMessage;


    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getMessage() {
        return clientMessage;
    }
}
