package com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads;

import com.anyqn.mastodon.common.models.deserializers.HttpUrlDeserialize;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.mastosync.models.twitter.FutureTweet;
import com.anyqn.mastodon.mastosync.processors.visitors.IMastodonPayloadVisitor;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.Map;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Jacksonized
@Builder
public class StatusPayload extends AbstractPayload {
    @Getter
    @EqualsAndHashCode.Include
    MastodonStatusId id;
    @JsonProperty("created_at")
    ZonedDateTime createdAt;
    @JsonProperty("in_reply_to_id")
    @Nullable
    MastodonStatusId inReplyToId;
    boolean sensitive;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = HttpUrlDeserialize.class)
    HttpUrl url;
    String content;
    Application application;
    Account account;

    @JsonAnySetter
    @Builder.Default
    Map<String, Object> additionalProperties = Map.of();

    @Override
    public String toString() {
        return "PostCreatedPayload{" +
                "id=" + id +
                ", inReplyToId=" + inReplyToId +
                ", url=" + url +
                ", content='" + StringUtils.abbreviateMiddle(content, "...", 64) + '\'' +
                ", account=" + account +
                '}';
    }

    @Override
    public FutureTweet visit(boolean eventFromMainUser, @Nullable TweetId parentTweetId, IMastodonPayloadVisitor visitor) {
        return visitor.visit(eventFromMainUser, parentTweetId, this);
    }
}