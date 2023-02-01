package com.anyqn.mastodon.common.models.mastodon;

import com.anyqn.mastodon.common.enums.Scope;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccessToken;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Jacksonized
@Builder
public class MastodonTokenResponse {
    @JsonAlias("access_token")
    private final MastodonAccessToken accessToken;
    @JsonAlias("token_type")
    private final String tokenType;
    private final Set<Scope> scopes;
    @JsonAlias("created_at")
    private final Timestamp createdAt;
}
