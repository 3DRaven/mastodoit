package com.anyqn.mastodon.mastosync.repostitories.twitter;

import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.models.twitter.TwitterCallDescriptor;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessToken;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessTokenSecret;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.common.util.OkHttpUtils;
import com.anyqn.mastodon.mastosync.models.twitter.FutureTweet;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterCreationResponse;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterDeletedResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.http.HttpMethod;

/**
 * https://developer.twitter.com/en/docs/twitter-api/tweets/manage-tweets/api-reference
 */
@Slf4j
public class AuthorizedTweetsRepository extends AbstractAuthorizedTwitterRemoteCallRepository {

    private static final HttpUrl manageTweetsUrl = Fluent.returnAndRethrow(() ->
            HttpUrl.get("https://api.twitter.com/2/tweets"));

    public HttpMethod getCreateTweetMethod() {
        return HttpMethod.POST;
    }

    public TwitterCreationResponse createTweet(TwitterUserAccessToken twitterAccessToken,
                                               TwitterUserAccessTokenSecret twitterAccessTokenSecret,
                                               FutureTweet twitterCreationRequest) {
        RequestBody body = RequestBody.create(
                Fluent.returnAndRethrow(() -> objectMapper.writeValueAsString(twitterCreationRequest)),
                Constants.HTTP_CLIENT_MEDIA_TYPE_JSON);
        Request request = new Request.Builder()
                .url(manageTweetsUrl)
                .post(body)
                .build();
        TwitterCallDescriptor twitterCallDescriptor = TwitterCallDescriptor.builder()
                .callMethod(getCreateTweetMethod())
                .calledUrl(manageTweetsUrl)
                .accessToken(twitterAccessToken)
                .accessTokenSecret(twitterAccessTokenSecret)
                .build();

        return Fluent.returnAndRethrow(() -> OkHttpUtils.getResponseModel(
                twitterAuthorizedOkHttpClientFabric.getInstance(twitterCallDescriptor),
                request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                        TwitterCreationResponse.class))));
    }

    public TwitterDeletedResponse deleteTweet(TwitterUserAccessToken twitterUserAccessToken,
                                              TwitterUserAccessTokenSecret twitterUserAccessTokenSecret,
                                              TweetId tweetId) {
        HttpUrl deleteUrl = manageTweetsUrl.newBuilder().addPathSegment(tweetId.getValue()).build();
        // https://api.twitter.com/2/tweets/:id
        Request request = new Request.Builder()
                .url(deleteUrl)
                .delete()
                .build();
        TwitterCallDescriptor twitterCallDescriptor = TwitterCallDescriptor.builder()
                .callMethod(HttpMethod.DELETE)
                .calledUrl(deleteUrl)
                .accessToken(twitterUserAccessToken)
                .accessTokenSecret(twitterUserAccessTokenSecret)
                .build();
        return Fluent.returnAndRethrow(() -> OkHttpUtils.getResponseModel(
                twitterAuthorizedOkHttpClientFabric.getInstance(twitterCallDescriptor),
                request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                        TwitterDeletedResponse.class))));

    }
}
