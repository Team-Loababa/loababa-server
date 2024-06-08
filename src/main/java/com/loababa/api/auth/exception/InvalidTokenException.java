package com.loababa.api.auth.exception;

import com.loababa.api.common.exception.ClientErrorInfo;
import com.loababa.api.common.exception.LoababaForbiddenException;
import com.loababa.api.common.exception.ServerErrorInfo;

public class InvalidTokenException extends LoababaForbiddenException {

    public InvalidTokenException(ClientErrorInfo clientErrorInfo, ServerErrorInfo serverErrorInfo) {
        super(clientErrorInfo, serverErrorInfo);
    }

}
