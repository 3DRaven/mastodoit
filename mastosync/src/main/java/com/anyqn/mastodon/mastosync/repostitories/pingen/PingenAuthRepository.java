package com.anyqn.mastodon.mastosync.repostitories.pingen;

import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.enums.Scope;
import com.anyqn.mastodon.common.models.twitter.TwitterCallDescriptor;
import com.anyqn.mastodon.common.models.values.PingenHost;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAuthorizationCode;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.common.util.OkHttpUtils;
import com.anyqn.mastodon.mastosync.models.mastodon.MastodonPinUrlResponse;
import com.anyqn.mastodon.common.models.mastodon.MastodonTokenResponse;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterAuthHeaderResponse;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterPinUrlResponse;
import com.anyqn.mastodon.mastosync.repostitories.AbstractUnauthorizedRemoteCallRepository;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Set;
import java.util.stream.Collectors;

public class PingenAuthRepository extends AbstractUnauthorizedRemoteCallRepository {

    public MastodonPinUrlResponse getMastodonCodeUrl(PingenHost pingenHost,
                                                     MastodonHost mastodonHost,
                                                     Set<Scope> scopes) {
        Request request = new Request.Builder()
                .url(pingenHost.getUrl()
                        .newBuilder()
                        .addPathSegment("api")
                        .addPathSegment("v1")
                        .addPathSegments("auth")
                        .addPathSegments("url")
                        .addPathSegment("mastodon")
                        .addQueryParameter("host", mastodonHost.getUrl().host())
                        .addQueryParameter("scopes",
                                scopes.stream().map(Scope::getDescription).collect(Collectors.joining(",")))
                        .build())
                .get()
                .build();
        return OkHttpUtils.getResponseModel(okHttpClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                MastodonPinUrlResponse.class)));
    }

    public MastodonTokenResponse getMastodonAccessToken(PingenHost pingenHost,
                                                        MastodonHost mastodonHost,
                                                        MastodonAuthorizationCode mastodonCode,
                                                        Set<Scope> scopes) {
        //https://pin.anyqn.com/api/v1/mastodon/getAccessToken?host=mtdn.anyqn.com&code=XXX&scopes=READ+WRITE
        Request request = new Request.Builder()
                .url(pingenHost
                        .getUrl()
                        .newBuilder()
                        .addPathSegment("api")
                        .addPathSegment("v1")
                        .addPathSegments("auth")
                        .addPathSegments("token")
                        .addPathSegment("mastodon")
                        .addQueryParameter("host", mastodonHost.getUrl().host())
                        .addQueryParameter("code", mastodonCode.getValue())
                        .addQueryParameter("scopes", scopes.stream().map(Scope::getDescription).collect(Collectors.joining(
                                ",")))
                        .build())
                .get()
                .build();
        return OkHttpUtils.getResponseModel(okHttpClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                MastodonTokenResponse.class)));
    }

    public TwitterPinUrlResponse getTwitterPinUrl(PingenHost pingenHost) {
        //https://pin.anyqn.com/api/v1/twitter/getAuthUrl
        Request request = new Request.Builder()
                .url(pingenHost
                        .getUrl()
                        .newBuilder()
                        .addPathSegment("api")
                        .addPathSegment("v1")
                        .addPathSegments("auth")
                        .addPathSegments("url")
                        .addPathSegment("twitter")
                        .build())
                .get()
                .build();
        return OkHttpUtils.getResponseModel(okHttpClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                TwitterPinUrlResponse.class)));
    }

    public TwitterAuthHeaderResponse getTwitterAuthHeader(PingenHost pingenHost,
                                                          TwitterCallDescriptor callDescriptor) {
        RequestBody body = RequestBody.create(
                Fluent.returnAndRethrow(() -> objectMapper.writeValueAsString(callDescriptor)),
                Constants.HTTP_CLIENT_MEDIA_TYPE_JSON);
        //https://pin.anyqn.com/api/v1/twitter/getAuthHeader
        Request request = new Request.Builder()
                .url(pingenHost
                        .getUrl()
                        .newBuilder()
                        .addPathSegment("api")
                        .addPathSegment("v1")
                        .addPathSegments("auth")
                        .addPathSegments("token")
                        .addPathSegment("twitter")
                        .build())
                .post(body)
                .build();
        return OkHttpUtils.getResponseModel(okHttpClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                TwitterAuthHeaderResponse.class)));
    }
}