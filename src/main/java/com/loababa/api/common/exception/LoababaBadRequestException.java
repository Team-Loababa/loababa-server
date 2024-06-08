package com.loababa.api.common.exception;

public class LoababaBadRequestException extends LoababaException {

    public LoababaBadRequestException(ClientErrorInfo clientErrorInfo, ServerErrorInfo serverErrorInfo) {
        super(clientErrorInfo, serverErrorInfo);
    }

}
