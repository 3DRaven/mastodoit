package com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads;

import com.anyqn.mastodon.common.models.deserializers.HttpUrlDeserialize;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccountId;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import okhttp3.HttpUrl;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Value
@Jacksonized
@Builder
public class Account {
    MastodonAccountId id;
    String username;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = HttpUrlDeserialize.class)
    HttpUrl url;

    @JsonAnySetter
    @Builder.Default
    Map<String, Object> additionalProperties = Map.of();

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", url=" + url +
                '}';
    }
}