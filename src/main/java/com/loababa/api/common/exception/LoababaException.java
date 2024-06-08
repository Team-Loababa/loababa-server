package com.loababa.api.common.exception;

public class LoababaException extends RuntimeException {

    private final ClientErrorInfo clientErrorInfo;

    public LoababaException(ClientErrorInfo clientErrorInfo, ServerErrorInfo serverErrorInfo) {
        super(serverErrorInfo.message(), serverErrorInfo.cause());
        this.clientErrorInfo = clientErrorInfo;
    }

    public String getClientCode() {
        return clientErrorInfo.getCode();
    }

    public String getClientMessage() {
        return clientErrorInfo.getMessage();
    }

}
