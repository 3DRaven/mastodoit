package com.anyqn.mastodon.mastosync.models.mastodon;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.ZonedDateTime;
import java.util.Map;

@Value
@Jacksonized
@Builder
public class MastodonPostCreatedResponse {
    MastodonStatusId id;

    @JsonAlias("created_at")
    ZonedDateTime createdAt;

    String content;

    @JsonAnySetter
    @Builder.Default
    Map<String, Object> application = Map.of();
}
