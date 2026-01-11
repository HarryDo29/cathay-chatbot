package com.cathay.identify.exception;

public class AppException extends  RuntimeException{
    private ErrorCode errCode;

    public AppException (ErrorCode errorCode){
        this.errCode = errorCode;
    }

    public ErrorCode getErrCode() {
        return errCode;
    }
}
