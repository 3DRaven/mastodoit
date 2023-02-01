package com.anyqn.mastodon.mastosync.repostitories.twitter;

import com.anyqn.mastodon.mastosync.fabrics.ObjectMapperFabric;
import com.anyqn.mastodon.mastosync.fabrics.clients.TwitterAuthorizedOkHttpClientFabric;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractAuthorizedTwitterRemoteCallRepository {
    protected final TwitterAuthorizedOkHttpClientFabric twitterAuthorizedOkHttpClientFabric =
            TwitterAuthorizedOkHttpClientFabric.IT;
    protected final ObjectMapper objectMapper = ObjectMapperFabric.IT.getInstance();
}
