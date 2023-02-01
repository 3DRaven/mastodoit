package com.anyqn.mastodon.common.models.mastodon;

import com.anyqn.mastodon.common.models.deserializers.HttpUrlDeserialize;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonClientId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import okhttp3.HttpUrl;

@Value
@Jacksonized
@Builder
public class MastodonAuthUrlResponse {
    @JsonIgnore
    MastodonClientId clientId;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = HttpUrlDeserialize.class)
    HttpUrl authorizationUrl;
}
