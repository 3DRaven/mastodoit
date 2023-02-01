package com.anyqn.mastodon.common.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class HttpResponseException extends RuntimeException {
    int code;
    String message;
    String responseBody;

    public HttpResponseException(int code, String message, String responseBody) {
        super(message);
        this.code = code;
        this.message = message;
        this.responseBody = responseBody;
    }
}
