package com.anyqn.mastodon.common.models.mastodon;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class MastodonRegistrationRequest {
    @JsonIgnore
    MastodonHost host;
    @JsonIgnore
    String registrationUrl = "/api/v1/apps";
    @JsonProperty("client_name")
    String clientName = "mtdn2twtr";
    @JsonProperty("redirect_uris")
    String redirectUris = "urn:ietf:wg:oauth:2.0:oob";
    String scopes = "read write";
    String website = "https://mtdn.anyqn.com";
}
