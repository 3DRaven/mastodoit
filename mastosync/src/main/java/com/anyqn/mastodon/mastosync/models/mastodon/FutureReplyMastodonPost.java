package com.anyqn.mastodon.mastosync.models.mastodon;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FutureReplyMastodonPost {
    @JsonIgnore
    String idempotencyKey;
    String status;
    @JsonAlias("in_reply_to_id")
    MastodonStatusId inReplyToId;
    boolean sensitive;
}
