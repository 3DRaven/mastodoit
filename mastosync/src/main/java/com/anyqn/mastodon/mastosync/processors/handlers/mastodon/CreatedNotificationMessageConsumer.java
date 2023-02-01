package com.anyqn.mastodon.mastosync.processors.handlers.mastodon;

import com.anyqn.mastodon.mastosync.enums.MessageSource;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.Message;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.MentionPayload;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreatedNotificationMessageConsumer extends CreatedStatusMessageConsumer {
    @Override
    protected boolean isValid(Message message) {
        return MentionPayload.class.isAssignableFrom(message.getPayload().getClass());
    }

    @Override
    protected void acceptImpl(Message message, MessageSource messageSource) {
        super.acceptImpl(message.toBuilder().payload(((MentionPayload) message.getPayload()).getStatus()).build(), messageSource);
    }
}