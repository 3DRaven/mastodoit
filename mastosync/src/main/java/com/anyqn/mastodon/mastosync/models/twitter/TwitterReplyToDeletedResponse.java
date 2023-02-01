package com.anyqn.mastodon.mastosync.models.twitter;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public class TwitterReplyToDeletedResponse {
    //{"detail":"You attempted to reply to a Tweet that is deleted or not visible to you.",
    // "type":"about:blank",
    // "title":"Forbidden",
    // "status":403}

    private final String detail;
    private final String type;
    private final String title;
    private final String status;
}
