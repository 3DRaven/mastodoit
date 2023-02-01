package com.anyqn.mastodon.mastosync.processors.handlers.mastodon;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.mastosync.enums.MessageSource;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.Message;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.PostDeletedPayload;
import com.anyqn.mastodon.mastosync.services.mastodon.MastodonPostsService;
import com.anyqn.mastodon.mastosync.services.twitter.TwitterTweetsService;

public class DeletedStatusMessageConsumer extends BaseMessageConsumer {

    private final TwitterTweetsService twitterTweetsService = SimpleContextFabric.IT.getInstance(TwitterTweetsService.class);
    private final MastodonPostsService mastodonPostsService = SimpleContextFabric.IT.getInstance(MastodonPostsService.class);

    @Override
    protected void acceptImpl(Message message, MessageSource messageSource) {
        mastodonPostsService.getAssociatedTweetId(((PostDeletedPayload)message.getPayload()).getId(), null)
                .ifPresent(twitterTweetsService::deleteTweet);
    }

    @Override
    protected boolean isValid(Message message) {
        return PostDeletedPayload.class.isAssignableFrom(message.getPayload().getClass());
    }
}
