package com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads;

import com.anyqn.mastodon.common.models.deserializers.HttpUrlDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Application {
    @JsonProperty("name")
    String name;
    @JsonProperty("website")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = HttpUrlDeserialize.class)
    HttpUrl website;
}