package com.anyqn.mastodon.mastosync.models.mastodon;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import okhttp3.HttpUrl;

@Data
@Jacksonized
@Builder
public class MastodonPinUrlResponse {
    private final String authorizationUrl;

    public HttpUrl getAuthorizationUrl() {
        return HttpUrl.get(authorizationUrl);
    }
}
