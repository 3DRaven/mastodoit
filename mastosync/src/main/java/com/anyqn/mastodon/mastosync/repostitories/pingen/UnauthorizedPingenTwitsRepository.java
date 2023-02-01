package com.anyqn.mastodon.mastosync.repostitories.pingen;

import com.anyqn.mastodon.common.models.twitter.tweets.TweetsGroup;
import com.anyqn.mastodon.common.models.values.PingenHost;
import com.anyqn.mastodon.common.models.values.twitter.*;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.common.util.OkHttpUtils;
import com.anyqn.mastodon.mastosync.models.twitter.TweetsGroupResponse;
import com.anyqn.mastodon.mastosync.repostitories.AbstractUnauthorizedRemoteCallRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import static com.anyqn.mastodon.common.Constants.*;


@Slf4j
public class UnauthorizedPingenTwitsRepository extends AbstractUnauthorizedRemoteCallRepository {

    @SneakyThrows
    public HttpUrl getUserTweetsUrl(PingenHost pingenHost, TwitterUserId id) {
        return pingenHost
                .getUrl()
                .newBuilder()
                .addPathSegment("api")
                .addPathSegment("v1")
                .addPathSegment("twitter")
                .addPathSegment("users")
                .addPathSegment(id.getValue())
                .addPathSegment("tweets")
                .build();
    }

    public HttpMethod getUserTweetsMethod() {
        return HttpMethod.GET;
    }

    public TweetsGroup getLastTweets(TwitterUserAccessToken accessToken,
                                     TwitterUserAccessTokenSecret accessTokenSecret,
                                     PingenHost pingenHost,
                                     TwitterUserId userId) {
        Request request = new Request.Builder()
                .get()
                .url(getUserTweetsUrl(pingenHost, userId))
                .addHeader(PINGEN_TWITTER_ACCESS_TOKEN_HEADER, accessToken.getValue())
                .addHeader(PINGEN_TWITTER_ACCESS_TOKEN_SECRET_HEADER, accessTokenSecret.getValue())
                .build();
        return OkHttpUtils.getResponseModel(okHttpClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                TweetsGroup.class)));
    }

    public TweetsGroupResponse getTweetsRange(TwitterUserAccessToken accessToken,
                                              TwitterUserAccessTokenSecret accessTokenSecret,
                                              TwitterUserId userId,
                                              PingenHost pingenHost,
                                              TweetId sinceIdExcluded,
                                              @Nullable TweetId untilIdExcluded) {
        Assert.isTrue(!sinceIdExcluded.equals(untilIdExcluded), String.format("Empty set of fetched tweets ids from [%s] to [%s]",
                sinceIdExcluded, untilIdExcluded));

        HttpUrl.Builder baseUrl = getUserTweetsUrl(pingenHost, userId)
                .newBuilder()
                .addQueryParameter(PINGEN_TWITTER_SINCE_ID_EXCLUDED_PARAM, sinceIdExcluded.getValue());

        if (null != untilIdExcluded) {
            baseUrl.addQueryParameter(PINGEN_TWITTER_UNTIL_ID_EXCLUDED_PARAM, untilIdExcluded.getValue());
        }

        Request request = new Request.Builder()
                .get()
                .url(baseUrl.build())
                .addHeader(PINGEN_TWITTER_ACCESS_TOKEN_HEADER, accessToken.getValue())
                .addHeader(PINGEN_TWITTER_ACCESS_TOKEN_SECRET_HEADER, accessTokenSecret.getValue())
                .build();

        TweetsGroup tweetsGroup = OkHttpUtils.getResponseModel(okHttpClient, request,
                it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                        TweetsGroup.class)));

        return TweetsGroupResponse.builder().tweetsGroup(tweetsGroup).sinceId(sinceIdExcluded).untilId(untilIdExcluded).build();
    }

    public TweetsGroup getTweetsPage(TwitterUserAccessToken accessToken,
                                     TwitterUserAccessTokenSecret accessTokenSecret,
                                     TwitterUserId userId,
                                     PingenHost pingenHost,
                                     TwitterPaginationToken paginationToken) {
        Request request = new Request.Builder()
                .get()
                .url(getUserTweetsUrl(pingenHost, userId)
                        .newBuilder()
                        .addQueryParameter(PINGEN_TWITTER_PAGE_TOKEN_PARAM, paginationToken.getValue())
                        .build())
                .addHeader(PINGEN_TWITTER_ACCESS_TOKEN_HEADER, accessToken.getValue())
                .addHeader(PINGEN_TWITTER_ACCESS_TOKEN_SECRET_HEADER, accessTokenSecret.getValue())
                .build();
        return OkHttpUtils.getResponseModel(okHttpClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                TweetsGroup.class)));
    }

}
