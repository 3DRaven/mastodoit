package com.anyqn.mastodon.mastosync.repostitories.mastodon;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.mastosync.controllers.AbstractSubscriptionEventListener;
import com.anyqn.mastodon.mastosync.fabrics.clients.MastodonOkHttpMainUserAuthorizedClientFabric;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.anyqn.mastodon.common.Constants.NORMAL_EXIT_RESPONSE_CODE;

public class MastodonSubscriptionRepository {
    private final OkHttpClient client = MastodonOkHttpMainUserAuthorizedClientFabric.IT.getMainUserHttpClient();
    private @Nullable WebSocket connection = null;
    private boolean connectionInProgress = false;

    public Response checkServerHealth(MastodonHost mastodonHost) {
        Request healthCheckerRequest = new Request.Builder()
                .url(mastodonHost
                        .getUrl()
                        .newBuilder()
                        .addPathSegment("api")
                        .addPathSegment("v1")
                        .addPathSegment("streaming")
                        .addPathSegment("health")
                        .build())
                .get()
                .build();

        return Fluent.returnAndRethrow(() -> client.newCall(healthCheckerRequest).execute());
    }

    public synchronized void subscribe(MastodonHost mastodonHost,
                                       AbstractSubscriptionEventListener eventListener) {
        if (!connectionInProgress) {
            connectionInProgress = true;
            CompletableFuture.runAsync(() -> {
                try {
                    eventListener.onConnectionStarting(mastodonHost);
                    Request request = new Request.Builder()
                            .url(mastodonHost
                                    .getUrl()
                                    .newBuilder()
                                    .addPathSegment("api")
                                    .addPathSegment("v1")
                                    .addPathSegment("streaming")
                                    .addQueryParameter("stream", "user")
                                    .build())
                            .build();
                    connection = client.newWebSocket(request, eventListener);
                    //Just for prevent closing problems
                    client.dispatcher().executorService().shutdown();
                } catch (Throwable throwable) {
                    eventListener.onConnectionError(throwable);
                }
            });
        }
    }

    public boolean isConnectionInProgress() {
        return connectionInProgress;
    }

    public synchronized void disconnect() {
        if (null != connection) {
            try {
                connection.close(NORMAL_EXIT_RESPONSE_CODE, "Exit");
            } catch (Throwable throwable) {
                connection.cancel();
                throw throwable;
            } finally {
                connection = null;
                connectionInProgress = false;
            }
        }
    }
}
