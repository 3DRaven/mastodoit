package com.anyqn.mastodon.common.models.twitter;

import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.models.deserializers.HttpUrlDeserialize;
import com.anyqn.mastodon.common.models.values.twitter.TwitterConsumerKey;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessToken;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessTokenSecret;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import okhttp3.HttpUrl;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

@Value
@Jacksonized
@Builder
public class TwitterAuthUrlResponse {
    TwitterUserAccessToken oauthToken;
    @JsonIgnore
    TwitterConsumerKey twitterConsumerKey;
    @JsonIgnore
    TwitterUserAccessTokenSecret oauthTokenSecret;
    @JsonIgnore
    boolean oauthCallbackConfirmed;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = HttpUrlDeserialize.class)
    HttpUrl authorizationUrl;

    @SneakyThrows
    public static TwitterAuthUrlResponse from(TwitterConsumerKey twitterConsumerKey, String response) {
        var builder = TwitterAuthUrlResponse.builder();
        Arrays.stream(StringUtils.tokenizeToStringArray(response, "&")).forEach(field -> {
            var pair = field.split("=");

            switch (pair[Constants.TWITTER_FIELD_NAME]) {
                case Constants.TWITTER_RESPONSE_OAUTH_TOKEN_FIELD:
                    builder.oauthToken(new TwitterUserAccessToken(URLDecoder.decode(pair[Constants.TWITTER_FIELD_VALUE],
                            StandardCharsets.UTF_8)));
                    break;
                case Constants.TWITTER_RESPONSE_OAUTH_TOKEN_SECRET_FIELD:
                    builder.oauthTokenSecret(new TwitterUserAccessTokenSecret(URLDecoder.decode(pair[Constants.TWITTER_FIELD_VALUE],
                            StandardCharsets.UTF_8)));
                    break;
                case Constants.TWITTER_RESPONSE_OAUTH_CALLBACK_CONFIRMED_FIELD:
                    builder.oauthCallbackConfirmed(Boolean.parseBoolean(URLDecoder.decode(pair[Constants.TWITTER_FIELD_VALUE], StandardCharsets.UTF_8)));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown field");
            }

        });

        builder.authorizationUrl(Objects.requireNonNull(HttpUrl.get(Constants.TWITTER_FINAL_URL_BASE + builder.oauthToken)));
        builder.twitterConsumerKey(twitterConsumerKey);
        return builder.build();
    }

    @Override
    public String toString() {
        return "TwitterAuthUrlResponse{ oauthToken='***'" +
                ", consumerKey='" + twitterConsumerKey + '\'' +
                ", oauthTokenSecret='***'" +
                ", oauthCallbackConfirmed=" + oauthCallbackConfirmed +
                ", authorizationUrl=" + authorizationUrl +
                '}';
    }
}
