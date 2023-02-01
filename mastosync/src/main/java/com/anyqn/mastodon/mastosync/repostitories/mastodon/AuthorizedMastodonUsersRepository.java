package com.anyqn.mastodon.mastosync.repostitories.mastodon;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.common.util.OkHttpUtils;
import com.anyqn.mastodon.mastosync.fabrics.ObjectMapperFabric;
import com.anyqn.mastodon.mastosync.fabrics.clients.MastodonOkHttpMainUserAuthorizedClientFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.account.MastodonAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AuthorizedMastodonUsersRepository {
    private final OkHttpClient mainUserHttpClient = MastodonOkHttpMainUserAuthorizedClientFabric.IT.getMainUserHttpClient();
    private final OkHttpClient replyUserHttpClient =
            MastodonOkHttpMainUserAuthorizedClientFabric.IT.getReplyUserHttpClient();
    private final ObjectMapper objectMapper = ObjectMapperFabric.IT.getInstance();

    public MastodonAccount getMastodonMainUserAccount(MastodonHost mastodonHost) {
        return getMastodonUserAccount(mastodonHost, mainUserHttpClient);
    }

    public MastodonAccount getMastodonReplyUserAccount(MastodonHost mastodonHost) {
        return getMastodonUserAccount(mastodonHost, replyUserHttpClient);
    }

    private MastodonAccount getMastodonUserAccount(MastodonHost mastodonHost, OkHttpClient client) {
        Request request = new Request.Builder()
                .url(mastodonHost
                        .getUrl()
                        .newBuilder()
                        .addPathSegment("api")
                        .addPathSegment("v1")
                        .addPathSegment("accounts")
                        .addPathSegment("verify_credentials")
                        .build())
                .get()
                .build();
        return OkHttpUtils.getResponseModel(client, request, it -> Fluent.returnAndRethrow(() -> objectMapper.readValue(it,
                MastodonAccount.class)));
    }

}
