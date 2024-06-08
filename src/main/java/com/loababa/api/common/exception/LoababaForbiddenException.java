package com.loababa.api.common.exception;

public class LoababaForbiddenException extends LoababaException{

    public LoababaForbiddenException(ClientErrorInfo clientErrorInfo, ServerErrorInfo serverErrorInfo) {
        super(clientErrorInfo, serverErrorInfo);
    }

}
