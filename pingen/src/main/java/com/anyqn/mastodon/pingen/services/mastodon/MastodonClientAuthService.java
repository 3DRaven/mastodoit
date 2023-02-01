package com.anyqn.mastodon.pingen.services.mastodon;

import com.anyqn.mastodon.common.models.mastodon.MastodonRegistrationRequest;
import com.anyqn.mastodon.common.models.mastodon.MastodonRegistrationResponse;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import com.anyqn.mastodon.pingen.models.cache.ClientCredentials;
import com.anyqn.mastodon.pingen.repository.mastodon.MastodonAppRegistrationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MastodonClientAuthService {

    private final MastodonAppRegistrationsRepository mastodonAppRegistrationsRepository;

    @Cacheable(value = "registrations", key = "#host.getUrl().host()")
    public ClientCredentials getClientCredentials(MastodonHost host) {
        MastodonRegistrationResponse registrationResponse = mastodonAppRegistrationsRepository.registerApp(new MastodonRegistrationRequest(host));
        return ClientCredentials.builder()
                .clientId(registrationResponse.getClientId())
                .clientSecret(registrationResponse.getClientSecret())
                .build();
    }
}
