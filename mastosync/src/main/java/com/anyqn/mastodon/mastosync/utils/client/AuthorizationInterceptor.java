package com.anyqn.mastodon.mastosync.utils.client;

import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.function.Function;

@Slf4j
public class AuthorizationInterceptor implements Interceptor {

    private final ConfigurationStateService configurationStateService;
    private final Function<Boolean, String> tokenSupplier;

    public AuthorizationInterceptor(ConfigurationStateService configurationStateService,
                                    Function<Boolean, String> tokenSupplier) {
        this.configurationStateService = configurationStateService;
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public Response intercept(Chain chain) {
        final int maxAttempts = configurationStateService.getRetryAttempts();
        int attempts = 0;
        @Nullable Response response = null;
        boolean forceHetHeader = false;
        do {
            if (null != response) {
                response.close();
            }
            Request request = chain.request();
            Request authorizedRequest = request.newBuilder()
                    .header(HttpHeaders.AUTHORIZATION, tokenSupplier.apply(forceHetHeader))
                    .build();
            forceHetHeader = true;
            //We do not close response immediately here because we will close it in outer code
            response = Fluent.returnAndRethrow(() -> chain.proceed(authorizedRequest));
            if (response.code() != HttpStatus.UNAUTHORIZED.value()) {
                break;
            }
        } while (++attempts < maxAttempts);

        return response;
    }
}