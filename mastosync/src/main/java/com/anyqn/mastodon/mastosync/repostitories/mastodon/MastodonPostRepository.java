package com.anyqn.mastodon.mastosync.repostitories.mastodon;

import com.anyqn.mastodon.common.Constants;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccountId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonNotificationId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.common.util.OkHttpUtils;
import com.anyqn.mastodon.mastosync.fabrics.ObjectMapperFabric;
import com.anyqn.mastodon.mastosync.fabrics.clients.MastodonOkHttpMainUserAuthorizedClientFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.FutureReplyMastodonPost;
import com.anyqn.mastodon.mastosync.models.mastodon.MastodonPostCreatedResponse;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.MentionPayload;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.StatusPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpScheme;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MastodonPostRepository {
    private final OkHttpClient mainUserHttpClient = MastodonOkHttpMainUserAuthorizedClientFabric.IT.getMainUserHttpClient();
    private final OkHttpClient replyUserHttpClient = MastodonOkHttpMainUserAuthorizedClientFabric.IT.getReplyUserHttpClient();
    private final ObjectMapper objectMapper = ObjectMapperFabric.IT.getInstance();

    public MastodonPostCreatedResponse createReplyPost(MastodonHost mastodonHost,
                                                       FutureReplyMastodonPost futureMastodonPost) {
        RequestBody body = RequestBody.create(
                Fluent.returnAndRethrow(() -> objectMapper.writeValueAsString(futureMastodonPost)),
                Constants.HTTP_CLIENT_MEDIA_TYPE_JSON);

        //POST /api/v1/statuses
        Request request = new Request.Builder()
                .url(mastodonHost
                        .getUrl()
                        .newBuilder()
                        .addPathSegment("api")
                        .addPathSegment("v1")
                        .addPathSegment("statuses")
                        .build())
                .post(body)
                .addHeader("Idempotency-Key", futureMastodonPost.getIdempotencyKey())
                .build();
        return OkHttpUtils.getResponseModel(replyUserHttpClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                MastodonPostCreatedResponse.class)));
    }

    public StatusPayload getMastodonMainUserPost(MastodonHost mastodonHost,
                                                 MastodonStatusId mastodonStatusId) {
        //GET /api/v1/statuses/:id
        Request request = new Request.Builder()
                .url(mastodonHost
                        .getUrl()
                        .newBuilder()
                        .addPathSegment("api")
                        .addPathSegment("v1")
                        .addPathSegment("statuses")
                        .addPathSegment(mastodonStatusId.getValue())
                        .build())
                .get()
                .build();
        return OkHttpUtils.getResponseModel(mainUserHttpClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                StatusPayload.class)));
    }

    public StatusPayload getTimelinePublic(MastodonHost mastodonHost, MastodonStatusId mastodonStatusId) {
        Request request = new Request.Builder()
                .url(mastodonHost
                        .getUrl()
                        .newBuilder()
                        .scheme(HttpScheme.HTTPS.toString())
                        .addPathSegment("api")
                        .addPathSegment("v1")
                        .addPathSegment("timelines")
                        .addPathSegment("public")
                        .build())
                .get()
                .build();
        return OkHttpUtils.getResponseModel(mainUserHttpClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                StatusPayload.class)));
    }

    public List<MentionPayload> getNotifications(MastodonHost mastodonHost,
                                                 @Nullable MastodonNotificationId mastodonNotificationId) {

        HttpUrl.Builder urlBuilder = mastodonHost
                .getUrl()
                .newBuilder()
                .scheme(HttpScheme.HTTPS.toString())
                .addPathSegment("api")
                .addPathSegment("v1")
                .addPathSegment("notifications")
                .addQueryParameter("types", "mention");

        Request request = new Request.Builder()
                .url(Optional.ofNullable(mastodonNotificationId)
                        .map(it -> urlBuilder.addQueryParameter("since_id",
                                Objects.requireNonNull(mastodonNotificationId).getValue()))
                        .orElse(urlBuilder)
                        .build())
                .get()
                .addHeader(HttpHeaders.ACCEPT, "application/activity+json")
                .build();

        return OkHttpUtils.getResponseModel(mainUserHttpClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MentionPayload.class))));
    }

    public List<StatusPayload> getAccountStatuses(MastodonHost mastodonHost,
                                                  MastodonAccountId mastodonUserId,
                                                  @Nullable MastodonStatusId sinceId) {

        HttpUrl.Builder urlBuilder = mastodonHost
                .getUrl()
                .newBuilder()
                .scheme(HttpScheme.HTTPS.toString())
                .addPathSegment("api")
                .addPathSegment("v1")
                .addPathSegment("accounts")
                .addPathSegment(mastodonUserId.getValue())
                .addPathSegment("statuses")
                .addQueryParameter("exclude_reblogs", "true")
                .addQueryParameter("exclude_replies", "true");

        Request request = new Request.Builder()
                .url(Optional.ofNullable(sinceId)
                        .map(it -> urlBuilder.addQueryParameter("since_id",
                                Objects.requireNonNull(sinceId).getValue()))
                        .orElse(urlBuilder)
                        .build())
                .get()
                .addHeader(HttpHeaders.ACCEPT, "application/activity+json")
                .build();

        return OkHttpUtils.getResponseModel(mainUserHttpClient, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                objectMapper.getTypeFactory().constructCollectionType(List.class, StatusPayload.class))));
    }
}
