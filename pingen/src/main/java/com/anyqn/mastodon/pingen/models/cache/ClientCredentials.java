package com.anyqn.mastodon.pingen.models.cache;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonClientId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonClientSecret;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Value
@Jacksonized
@Builder
public class ClientCredentials implements Serializable {
    MastodonClientId clientId;
    MastodonClientSecret clientSecret;

    @Override
    public String toString() {
        return "ClientCredentials{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='***'" +
                '}';
    }
}
