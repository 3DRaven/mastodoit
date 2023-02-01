package com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.ZonedDateTime;

@Value
@Jacksonized
@Builder
public class Field {
    @JsonProperty("name")
    String name;
    @JsonProperty("value")
    String value;
    @JsonProperty("verified_at")
    ZonedDateTime verifiedAt;
}