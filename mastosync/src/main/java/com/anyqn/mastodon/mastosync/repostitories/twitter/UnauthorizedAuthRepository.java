package com.anyqn.mastodon.mastosync.repostitories.twitter;

import com.anyqn.mastodon.common.models.values.twitter.TwitterPinCode;
import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessToken;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.mastosync.models.twitter.TwitterAccessTokenResponse;
import com.anyqn.mastodon.mastosync.repostitories.AbstractUnauthorizedRemoteCallRepository;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.Optional;

public class UnauthorizedAuthRepository extends AbstractUnauthorizedRemoteCallRepository {

    public TwitterAccessTokenResponse getTwitterToken(TwitterPinCode code, TwitterUserAccessToken accessToken) {
        //https://api.twitter.com/oauth/access_token
        Request request = new Request.Builder()
                .url(HttpUrl.get("https://api.twitter.com")
                        .newBuilder()
                        .addPathSegment("oauth")
                        .addPathSegment("access_token")
                        .addQueryParameter("oauth_token", accessToken.getValue())
                        .addQueryParameter("oauth_verifier", code.getValue())
                        .build())
                .get()
                .build();

        try (Response response = Fluent.returnAndRethrow(() -> okHttpClient.newCall(request).execute())) {
            try (ResponseBody body = Optional.ofNullable(response.body()).orElseThrow()) {
                return TwitterAccessTokenResponse.from(Fluent.returnAndRethrow(body::string));
            }
        }
    }
}
