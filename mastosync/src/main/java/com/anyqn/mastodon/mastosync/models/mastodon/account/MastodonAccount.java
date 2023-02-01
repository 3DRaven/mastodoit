package com.anyqn.mastodon.mastosync.models.mastodon.account;

import com.anyqn.mastodon.common.models.deserializers.HttpUrlDeserialize;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccountId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import okhttp3.HttpUrl;

import java.time.ZonedDateTime;
import java.util.List;

@Value
@Jacksonized
@Builder
public class MastodonAccount {
    @JsonProperty("id")
    MastodonAccountId id;
    @JsonProperty("username")
    String username;
    @JsonProperty("acct")
    String acct;
    @JsonProperty("display_name")
    String displayName;
    @JsonProperty("locked")
    Boolean locked;
    @JsonProperty("bot")
    Boolean bot;
    @JsonProperty("discoverable")
    Boolean discoverable;
    @JsonProperty("group")
    Boolean group;
    @JsonProperty("created_at")
    ZonedDateTime createdAt;
    @JsonProperty("note")
    String note;
    @JsonProperty("url")
    @JsonDeserialize(using = HttpUrlDeserialize.class)
    HttpUrl url;
    @JsonProperty("avatar")
    String avatar;
    @JsonProperty("avatar_static")
    String avatarStatic;
    @JsonProperty("header")
    String header;
    @JsonProperty("header_static")
    String headerStatic;
    @JsonProperty("followers_count")
    Integer followersCount;
    @JsonProperty("following_count")
    Integer followingCount;
    @JsonProperty("statuses_count")
    Integer statusesCount;
    @JsonProperty("last_status_at")
    String lastStatusAt;
    @JsonProperty("source")
    Source source;
    @Builder.Default
    @JsonProperty("emojis")
    List<Object> emojis = List.of();
    @Builder.Default
    @JsonProperty("fields")
    List<Field> fields = List.of();
}