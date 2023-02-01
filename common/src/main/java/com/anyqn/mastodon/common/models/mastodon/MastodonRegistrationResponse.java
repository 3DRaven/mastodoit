package com.anyqn.mastodon.common.models.mastodon;

import com.anyqn.mastodon.common.models.deserializers.HttpUrlDeserialize;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccountId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonClientId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonClientSecret;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import okhttp3.HttpUrl;

@Value
@Jacksonized
@Builder
public class MastodonRegistrationResponse {
    MastodonAccountId id;
    String name;
    @JsonDeserialize(using = HttpUrlDeserialize.class)
    HttpUrl website;
    @JsonProperty("redirect_uri")
    String redirectUri; //urn:ietf:wg:oauth:2.0:oob
    @JsonProperty("client_id")
    MastodonClientId clientId;
    @JsonProperty("client_secret")
    MastodonClientSecret clientSecret;
    @JsonProperty("vapid_key")
    String vapidKey;
}
