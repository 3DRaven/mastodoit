package com.anyqn.mastodon.pingen.repository.mastodon;

import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.models.mastodon.MastodonRegistrationRequest;
import com.anyqn.mastodon.common.models.mastodon.MastodonRegistrationResponse;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.common.util.OkHttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpScheme;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class MastodonAppRegistrationsRepository {

    private final OkHttpClient mastodonWebClient;
    private final ObjectMapper objectMapper;


    public MastodonAppRegistrationsRepository(@Qualifier("mastodonWebClient") OkHttpClient mastodonWebClient,
                                              ObjectMapper objectMapper) {
        this.mastodonWebClient = mastodonWebClient;
        this.objectMapper = objectMapper;
    }

    public MastodonRegistrationResponse registerApp(MastodonRegistrationRequest mastodonRegistrationRequest) {
        //writeValueAsString is nonnull
        RequestBody body =
                RequestBody.create(
                        Fluent.returnAndRethrow(() -> objectMapper.writeValueAsString(mastodonRegistrationRequest)),
                        Constants.HTTP_CLIENT_MEDIA_TYPE_JSON);

        Request request = new Request.Builder()
                .url(mastodonRegistrationRequest
                        .getHost()
                        .getUrl()
                        .newBuilder()
                        .scheme(HttpScheme.HTTPS.toString())
                        .addPathSegments(mastodonRegistrationRequest.getRegistrationUrl())
                        .build())
                .post(body)
                .build();
        return OkHttpUtils.getResponseModel(mastodonWebClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                MastodonRegistrationResponse.class)));
    }
}
