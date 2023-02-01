package com.anyqn.mastodon.pingen.handlers;

import com.anyqn.mastodon.pingen.models.ApiError;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

@ControllerAdvice
public class ErrorsHandler extends ResponseEntityExceptionHandler {

    private final int numberOfThreads;

    public ErrorsHandler(@Value("${controller.numberOfThreads}") int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    @ResponseBody
    @ExceptionHandler(value
            = {RejectedExecutionException.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    protected ApiError handleConflict(RejectedExecutionException ex, WebRequest request) {
        return ApiError.builder().status(HttpStatus.TOO_MANY_REQUESTS.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .path(request.getContextPath())
                .message(String.format("Server rate limit is %d requests per second from all clients", numberOfThreads)).build();
    }

    @ResponseBody
    @ExceptionHandler(value = {ExecutionException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiError handleRemoteError(ExecutionException ex, WebRequest request) {
        var rootCause = Optional.ofNullable(ExceptionUtils.getRootCause(ex));
        var errorBuilder = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .path(request.getContextPath());

        if (rootCause.map(it -> WebClientResponseException.class.isAssignableFrom(it.getClass()))
                .orElse(false)) {
            return errorBuilder.message(String.format("Remote call status [%d] error body: [%s]", rootCause
                            .map(it -> ((WebClientResponseException) it).getStatusCode().value())
                            .orElse(-1), rootCause
                            .map(it -> ((WebClientResponseException) it).getResponseBodyAsString()
                            )
                            .orElse("UnknownMessage")))
                    .build();
        } else {
            return errorBuilder
                    .message(String.format("Unknown type of exception: [%s]",
                            rootCause
                                    .map(ExceptionUtils::getRootCauseMessage)
                                    .orElse("UnknownMessage")))
                    .build();
        }
    }
}