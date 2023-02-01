package com.anyqn.mastodon.common.models.twitter;

import com.anyqn.mastodon.common.models.deserializers.HttpUrlDeserialize;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessToken;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessTokenSecret;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import okhttp3.HttpUrl;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;

import java.util.Map;

@Value
@Jacksonized
@Builder
public class TwitterCallDescriptor {
    HttpMethod callMethod;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = HttpUrlDeserialize.class)
    HttpUrl calledUrl;
    /**
     * Attention. Twitter do not use json fields for calculating signature, only Form fields and query parameters
     */
    @Builder.Default
    Map<String, String> callParameters = Map.of();
    @Nullable
    TwitterUserAccessToken accessToken;
    @Nullable
    TwitterUserAccessTokenSecret accessTokenSecret;

    @Override
    public String toString() {
        return String.format("TwitterCallData{ calledMethod=%s calledUrl=%s, accessToken='***', " +
                "accessTokenSecret='***'" +
                " }", callMethod.name(), calledUrl);
    }
}
