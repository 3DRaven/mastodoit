package com.anyqn.mastodon.common.util;

import com.anyqn.mastodon.common.exceptions.HttpResponseException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
public abstract class OkHttpUtils {
    public static String getResponseBodyForLog(Response response) {
        return Optional.of(response)
                .map(Response::body)
                .map(it -> {
                    try {
                        return it.string();
                    } catch (Throwable throwable) {
                        log.error("Unable to get response body from Mastodon server", throwable);
                        return "Body reading error: " + ExceptionUtils.getRootCauseMessage(throwable);
                    }
                })
                .orElse("Empty response");
    }

    public static <T> T getResponseModel(OkHttpClient okHttpClient, Request request, Function<String, T> mapper) {
        @Nullable String bodyString = null;
        try (Response response = Fluent.returnAndRethrow(() -> okHttpClient.newCall(request).execute())) {
            //TODO: на релизе заменить стрингу на поток байт
            try (ResponseBody body = Optional.ofNullable(response.body()).orElseThrow()) {
                bodyString = Fluent.returnAndRethrow(body::string);
                if (response.isSuccessful()) {
                    return mapper.apply(bodyString);
                } else {
                    throw new HttpResponseException(response.code(), response.message(), bodyString);
                }
            }
        }
    }
}
