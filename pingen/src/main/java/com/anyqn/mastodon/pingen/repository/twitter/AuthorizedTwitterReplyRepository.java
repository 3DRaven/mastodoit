package com.anyqn.mastodon.pingen.repository.twitter;

import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.models.twitter.TwitterCallDescriptor;
import com.anyqn.mastodon.common.models.twitter.TwitterRequestData;
import com.anyqn.mastodon.common.models.twitter.tweets.TweetsGroup;
import com.anyqn.mastodon.common.models.values.twitter.*;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.common.util.OkHttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Repository
public class AuthorizedTwitterReplyRepository {

    private final OkHttpClient twitterWebClient;
    private final ObjectMapper objectMapper;

    public AuthorizedTwitterReplyRepository(@Qualifier("twitterWebClient") OkHttpClient twitterWebClient,
                                            ObjectMapper objectMapper) {
        this.twitterWebClient = twitterWebClient;
        this.objectMapper = objectMapper;
    }

    public HttpMethod getUserTweetsMethod() {
        return HttpMethod.GET;
    }

    public TweetsGroup getLastTweets(TwitterConsumerKey twitterConsumerKey,
                                     TwitterConsumerSecret twitterConsumerSecret,
                                     TwitterUserAccessToken accessToken,
                                     TwitterUserAccessTokenSecret accessTokenSecret,
                                     TwitterUserId userId,
                                     @Nullable TweetId sinceId,
                                     @Nullable TweetId untilId,
                                     @Nullable TwitterPaginationToken twitterPaginationToken) {

        Map<String, String> params = new HashMap<>();

        params.put(Constants.TWITTER_MAX_RESULTS_QUERY_PARAM_NAME,
                String.valueOf(Constants.TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST));
        params.put("expansions", "author_id,in_reply_to_user_id,entities.mentions.username,referenced_tweets.id" +
                ".author_id");
        params.put("user.fields", "description,id,name,username");
        params.put("tweet.fields", "attachments,author_id,context_annotations,conversation_id,created_at,entities," +
                "in_reply_to_user_id,possibly_sensitive,referenced_tweets,reply_settings");
        params.put("poll.fields", "duration_minutes,end_datetime,id,options,voting_status");
        params.put("media.fields", "preview_image_url,url,type");
        Optional.ofNullable(sinceId).ifPresent(id -> params.put(Constants.TWITTER_SINCE_ID, id.getValue()));
        Optional.ofNullable(untilId).ifPresent(id -> params.put(Constants.TWITTER_UNTIL_ID, id.getValue()));
        Optional.ofNullable(twitterPaginationToken).ifPresent(token -> params.put(Constants.TWITTER_PAGINATION_TOKEN,
                token.getValue()));

        HttpUrl.Builder urlBuilder = Constants.TWITTER_BASE_URL
                .newBuilder()
                .addPathSegment("2")
                .addPathSegment("users")
                .addPathSegment(userId.getValue())
                .addPathSegment("tweets");

        TwitterRequestData requestDescriptor = new TwitterRequestData(
                twitterConsumerKey,
                twitterConsumerSecret,
                TwitterCallDescriptor.builder()
                        .callMethod(getUserTweetsMethod())
                        .calledUrl(urlBuilder.build())
                        .accessToken(accessToken)
                        .accessTokenSecret(accessTokenSecret)
                        .callParameters(params)
                        .build());

        params.forEach(urlBuilder::addQueryParameter);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader(HttpHeaders.AUTHORIZATION, requestDescriptor.getAuthorizationHeader())
                .get()
                .build();

        return OkHttpUtils.getResponseModel(twitterWebClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                TweetsGroup.class)));
    }
}
