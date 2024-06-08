package com.loababa.api.auth.exception;

import com.loababa.api.common.exception.ClientErrorInfo;
import com.loababa.api.common.exception.LoababaBadGatewayException;
import com.loababa.api.common.exception.ServerErrorInfo;

public class ExternalCommunicationException extends LoababaBadGatewayException {

    public ExternalCommunicationException(ClientErrorInfo clientErrorInfo, ServerErrorInfo serverErrorInfo) {
        super(clientErrorInfo, serverErrorInfo);
    }

}
