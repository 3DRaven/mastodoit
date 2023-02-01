package com.anyqn.mastodon.mastosync.models.twitter;

import com.anyqn.mastodon.common.models.deserializers.HttpUrlDeserialize;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessToken;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import okhttp3.HttpUrl;

@Data
@Jacksonized
@Builder
public class TwitterPinUrlResponse {
    private final TwitterUserAccessToken oauthToken;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = HttpUrlDeserialize.class)
    private final HttpUrl authorizationUrl;
}
