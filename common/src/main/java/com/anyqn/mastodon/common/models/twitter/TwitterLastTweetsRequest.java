package com.anyqn.mastodon.common.models.twitter;

import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessToken;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessTokenSecret;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserId;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;

import java.time.ZonedDateTime;

@Value
@Jacksonized
@Builder
public class TwitterLastTweetsRequest {
    @Nullable
    ZonedDateTime from;
    @Nullable
    TweetId sinceId;
    
    TwitterUserId userId;
    
    TwitterUserAccessToken accessToken;
    
    TwitterUserAccessTokenSecret accessTokenSecret;

    @Override
    public String toString() {
        return String.format("TwitterCallData{ from=%s, sinceId=%s, userId=%s, accessToken='***', " +
                "accessTokenSecret='***'" +
                " }", from, sinceId, userId);
    }
}
