package com.loababa.api.common.exception;

public class LoababaBadGatewayException extends LoababaException {

    public LoababaBadGatewayException(ClientErrorInfo clientErrorInfo, ServerErrorInfo serverErrorInfo) {
        super(clientErrorInfo, serverErrorInfo);
    }

}
