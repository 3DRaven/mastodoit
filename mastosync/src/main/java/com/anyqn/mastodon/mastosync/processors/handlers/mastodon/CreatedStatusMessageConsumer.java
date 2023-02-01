package com.anyqn.mastodon.mastosync.processors.handlers.mastodon;

import com.anyqn.mastodon.mastosync.enums.MessageSource;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.Message;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.StatusPayload;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import com.anyqn.mastodon.mastosync.services.mastodon.MastodonPostsService;
import com.anyqn.mastodon.mastosync.services.mastodon.MastodonUserService;
import com.anyqn.mastodon.mastosync.services.twitter.TwitterTweetsService;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class CreatedStatusMessageConsumer extends BaseMessageConsumer {

    private final TwitterTweetsService twitterTweetsService = SimpleContextFabric.IT.getInstance(TwitterTweetsService.class);
    private final MastodonPostsService mastodonPostsService = SimpleContextFabric.IT.getInstance(MastodonPostsService.class);
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);
    private final MastodonUserService mastodonUserService =
            SimpleContextFabric.IT.getInstance(MastodonUserService.class);

    @Override
    protected void acceptImpl(Message message, MessageSource messageSource) {
        StatusPayload statusPayload = (StatusPayload) message.getPayload();
        if (configurationStateService.isRelease()) {
            boolean isEventFromMainUser = mastodonUserService.isEventFromMainUser(statusPayload);
            if (null != statusPayload.getInReplyToId()) {
                mastodonPostsService.getAssociatedTweetId(
                                //checked null in if
                                Objects.requireNonNull(statusPayload.getInReplyToId()),
                                statusPayload.getContent())
                        .ifPresent(associatedTweetId ->
                                twitterTweetsService.createTweetFromPost(
                                        messageSource,
                                        statusPayload,
                                        isEventFromMainUser,
                                        associatedTweetId));
            } else if (isEventFromMainUser) {
                twitterTweetsService.createTweetFromPost(
                        messageSource,
                        statusPayload,
                        true,
                        null);
            }
        } else {
            log.info("It is debug mode, Twitter listener disabled");
        }
    }

    @Override
    protected boolean isValid(Message message) {
        return StatusPayload.class.isAssignableFrom(message.getPayload().getClass());
    }
}