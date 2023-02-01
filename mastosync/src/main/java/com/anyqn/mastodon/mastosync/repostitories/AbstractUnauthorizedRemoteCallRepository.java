package com.anyqn.mastodon.mastosync.repostitories;

import com.anyqn.mastodon.mastosync.fabrics.ObjectMapperFabric;
import com.anyqn.mastodon.mastosync.fabrics.clients.UnauthorizedOkHttpClientFabric;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

public abstract class AbstractUnauthorizedRemoteCallRepository {
    protected final OkHttpClient okHttpClient = UnauthorizedOkHttpClientFabric.IT.getInstance();
    protected final ObjectMapper objectMapper = ObjectMapperFabric.IT.getInstance();
}
