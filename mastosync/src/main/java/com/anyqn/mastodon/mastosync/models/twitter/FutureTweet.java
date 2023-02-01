package com.anyqn.mastodon.mastosync.models.twitter;

import com.anyqn.mastodon.common.models.deserializers.HttpUrlDeserialize;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonNotificationId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.StatusPayload;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import static com.anyqn.mastodon.common.Constants.*;

@Value
@Jacksonized
@Builder
public class FutureTweet {
    String text;
    @JsonIgnore
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = HttpUrlDeserialize.class)
    HttpUrl replyUrl;

    @Nullable
    ReplyToTweet reply;

    @JsonIgnore
    MastodonStatusId mastodonStatusId;
    @JsonIgnore
    @Nullable
    MastodonNotificationId mastodonNotificationId;
    @JsonIgnore
    StatusPayload sourceStatus;

    @JsonGetter("text")
    public String getText() {
        return StringUtils.truncate(text, 250);
    }

    @Override
    public String toString() {
        return String.format("Tweet{text=[%s], textLength=[%d], urlLength=[%d], finalTextLength=[%d], reply=[%s]}",
                text,
                text.length(),
                replyUrl.toString().length(),
                TWEET_ALLOWED_MESSAGE_LENGTH_SYMBOLS +
                        TWEET_REPLY_TEXT_LENGTH_SYMBOLS +
                        TWEET_SHORTEN_LINK_SIZE,
                reply);
    }
}