package com.anyqn.mastodon.common.models.mastodon;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonClientId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonClientSecret;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class MastodonTokenRequest {
    @JsonProperty("client_id")
    MastodonClientId clientId;
    @JsonProperty("client_secret")
    MastodonClientSecret clientSecret;
    @JsonProperty("redirect_uri")
    String redirectUri = "urn:ietf:wg:oauth:2.0:oob";
    @JsonProperty("grant_type")
    String grantType = "client_credentials";
}
