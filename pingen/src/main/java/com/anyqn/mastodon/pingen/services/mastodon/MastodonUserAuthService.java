package com.anyqn.mastodon.pingen.services.mastodon;

import com.anyqn.mastodon.common.enums.Scope;
import com.anyqn.mastodon.common.models.mastodon.MastodonAuthUrlResponse;
import com.anyqn.mastodon.common.models.mastodon.MastodonTokenResponse;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAuthorizationCode;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import com.anyqn.mastodon.pingen.repository.mastodon.MastodonAccessTokenRepository;
import io.netty.handler.codec.http.HttpScheme;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class MastodonUserAuthService {
    private final MastodonAccessTokenRepository mastodonAccessTokenRepository;
    private final MastodonClientAuthService mastodonClientAuthService;

    @SneakyThrows
    public MastodonAuthUrlResponse getAuthUrl(MastodonHost host, Set<Scope> scope) {
        var clientCredentials = mastodonClientAuthService.getClientCredentials(host);
        return MastodonAuthUrlResponse.builder()
                .authorizationUrl(new HttpUrl.Builder()
                        .host(host.getUrl().host())
                        .scheme(HttpScheme.HTTPS.toString())
                        .addPathSegment("oauth")
                        .addPathSegment("authorize")
                        .addQueryParameter("response_type", "code")
                        .addQueryParameter("client_id", clientCredentials.getClientId().getValue())
                        .addQueryParameter("redirect_uri", "urn:ietf:wg:oauth:2.0:oob")
                        .addQueryParameter("scope",
                                scope.stream()
                                        .map(Scope::getDescription)
                                        .map(String::toLowerCase)
                                        .collect(Collectors.joining(" ")))
                        .addQueryParameter("force_login", "true")
                        .build())
                .clientId(clientCredentials.getClientId())
                .build();
    }

    public MastodonTokenResponse getUserAccessToken(MastodonHost host, MastodonAuthorizationCode authorizationCode,
                                                    Set<Scope> scopes) {
        var clientCredentials = mastodonClientAuthService.getClientCredentials(host);
        return mastodonAccessTokenRepository.getUserAccessToken(
                host,
                authorizationCode,
                clientCredentials.getClientId(),
                clientCredentials.getClientSecret(),
                scopes);
    }

}
