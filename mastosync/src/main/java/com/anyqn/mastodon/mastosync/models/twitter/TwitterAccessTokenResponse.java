package com.anyqn.mastodon.mastosync.models.twitter;

import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessToken;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessTokenSecret;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserId;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Arrays;

@Data
@Jacksonized
@Builder
public class TwitterAccessTokenResponse {

    private static final int TWITTER_FIELD_NAME = 0;
    private static final int TWITTER_FIELD_VALUE = 1;
    private static final String TWITTER_RESPONSE_OAUTH_TOKEN_FIELD = "oauth_token";
    private static final String TWITTER_RESPONSE_OAUTH_TOKEN_SECRET_FIELD = "oauth_token_secret";
    private static final String TWITTER_RESPONSE_USER_ID_FIELD = "user_id";
    private static final String TWITTER_RESPONSE_SCREEN_NAME_FIELD = "screen_name";
    @JsonAlias("oauth_token")
    private final TwitterUserAccessToken oauthToken;
    @JsonAlias("oauth_token_secret")
    private final TwitterUserAccessTokenSecret oauthTokenSecret;
    @JsonAlias("user_id")
    private final TwitterUserId userId;
    @JsonAlias("screen_name")
    private final String screenName;

    //oauth_token=xxxxxx&oauth_token_secret=xxxxxx&user_id=xxxxxx&screen_name=xxxxxxx
    public static TwitterAccessTokenResponse from(String response) {
        var builder = TwitterAccessTokenResponse.builder();
        Arrays.stream(response.split("&")).forEach(field -> {
            var pair = field.split("=");

            switch (pair[TWITTER_FIELD_NAME]) {
                case TWITTER_RESPONSE_OAUTH_TOKEN_FIELD:
                    builder.oauthToken(new TwitterUserAccessToken(pair[TWITTER_FIELD_VALUE]));
                    break;
                case TWITTER_RESPONSE_OAUTH_TOKEN_SECRET_FIELD:
                    builder.oauthTokenSecret(new TwitterUserAccessTokenSecret(pair[TWITTER_FIELD_VALUE]));
                    break;
                case TWITTER_RESPONSE_USER_ID_FIELD:
                    builder.userId(new TwitterUserId(pair[TWITTER_FIELD_VALUE]));
                    break;
                case TWITTER_RESPONSE_SCREEN_NAME_FIELD:
                    builder.screenName(pair[TWITTER_FIELD_VALUE]);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unrecognized response: [%s]", response));
            }
        });
        return builder.build();
    }


}
