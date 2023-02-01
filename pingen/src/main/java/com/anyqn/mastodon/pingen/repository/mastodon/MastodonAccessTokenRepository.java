package com.anyqn.mastodon.pingen.repository.mastodon;

import com.anyqn.mastodon.common.enums.Scope;
import com.anyqn.mastodon.common.models.mastodon.MastodonTokenResponse;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAuthorizationCode;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonClientId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonClientSecret;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.common.util.OkHttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpScheme;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class MastodonAccessTokenRepository {

    private final OkHttpClient mastodonWebClient;
    private final ObjectMapper objectMapper;

    public MastodonAccessTokenRepository(@Qualifier("mastodonWebClient") OkHttpClient mastodonWebClient,
                                         ObjectMapper objectMapper) {
        this.mastodonWebClient = mastodonWebClient;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public MastodonTokenResponse getUserAccessToken(MastodonHost host,
                                                    MastodonAuthorizationCode authorizationCode,
                                                    MastodonClientId clientId,
                                                    MastodonClientSecret clientSecret,
                                                    Set<Scope> scopes) {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "authorization_code")
                .addFormDataPart("code", authorizationCode.getValue())
                .addFormDataPart("client_id", clientId.getValue())
                .addFormDataPart("client_secret", clientSecret.getValue())
                .addFormDataPart("redirect_uri", "urn:ietf:wg:oauth:2.0:oob")
                .addFormDataPart("scope", scopes.stream().map(Scope::getDescription)
                        .collect(Collectors.joining(" "))).
                build();


        Request request = new Request.Builder()
                .url(new HttpUrl.Builder()
                        .scheme(HttpScheme.HTTPS.toString())
                        .host(host.getUrl().host())
                        .addPathSegment("oauth")
                        .addPathSegment("token")
                        .build())
                .post(multipartBody)
                .build();
        return OkHttpUtils.getResponseModel(mastodonWebClient, request,
                it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                        MastodonTokenResponse.class)));
    }
}
