package com.anyqn.mastodon.pingen.repository.twitter;

import com.anyqn.mastodon.common.models.twitter.TwitterAuthUrlResponse;
import com.anyqn.mastodon.common.models.twitter.TwitterCallDescriptor;
import com.anyqn.mastodon.common.models.twitter.TwitterRequestData;
import com.anyqn.mastodon.common.models.values.twitter.TwitterConsumerKey;
import com.anyqn.mastodon.common.models.values.twitter.TwitterConsumerSecret;
import com.anyqn.mastodon.common.util.OkHttpUtils;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static com.anyqn.mastodon.common.Constants.*;

@Repository
public class TwitterAuthRepository {

    private final OkHttpClient twitterWebClient;

    public TwitterAuthRepository(@Qualifier("twitterWebClient") OkHttpClient twitterWebClient) {
        this.twitterWebClient = twitterWebClient;
    }

    @SneakyThrows
    public TwitterAuthUrlResponse getTwitterAuthUrl(TwitterConsumerKey consumerKey, TwitterConsumerSecret consumerSecret) {
        HttpUrl callUrl = TWITTER_BASE_URL.newBuilder()
                .addPathSegment("oauth")
                .addPathSegments("request_token")
                .build();

        TwitterRequestData requestData = new TwitterRequestData(
                consumerKey,
                consumerSecret,
                TwitterCallDescriptor.builder()
                        .calledUrl(callUrl)
                        .callMethod(HttpMethod.POST)
                        .callParameters(Map.of(TWITTER_QUERY_OAUTH_CALLBACK, TWITTER_CALLBACK_TYPE)).build()
        );

        Request request = new Request.Builder()
                .url(callUrl.newBuilder().addQueryParameter(TWITTER_QUERY_OAUTH_CALLBACK, TWITTER_CALLBACK_TYPE).build())
                .addHeader(HttpHeaders.AUTHORIZATION, requestData.getAuthorizationHeader())
                .post(RequestBody.create("", null))
                .build();

        return OkHttpUtils.getResponseModel(twitterWebClient, request, it -> TwitterAuthUrlResponse.from(consumerKey, it));
    }
}