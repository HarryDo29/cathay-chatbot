package com.cathay.apigateway.enums;

public enum AcceptType {
    APPLICATION_JSON("application/json"),
    APPLICATION_XML("application/xml"),
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html");

    private final String value;

    AcceptType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
