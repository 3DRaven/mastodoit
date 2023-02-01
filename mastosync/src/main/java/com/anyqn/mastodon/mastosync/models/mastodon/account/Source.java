package com.anyqn.mastodon.mastosync.models.mastodon.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Jacksonized
@Builder
public class Source {
    @JsonProperty("privacy")
    String privacy;
    @JsonProperty("sensitive")
    Boolean sensitive;
    @JsonProperty("language")
    String language;
    @JsonProperty("note")
    String note;
    @Builder.Default
    @JsonProperty("fields")
    List<Field> fields = List.of();
    @JsonProperty("follow_requests_count")
    Integer followRequestsCount;
}
