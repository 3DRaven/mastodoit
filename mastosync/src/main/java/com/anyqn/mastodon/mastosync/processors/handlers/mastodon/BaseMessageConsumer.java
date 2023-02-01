package com.anyqn.mastodon.mastosync.processors.handlers.mastodon;

import com.anyqn.mastodon.mastosync.enums.MessageSource;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.Message;

import java.util.function.BiConsumer;

public abstract class BaseMessageConsumer implements BiConsumer<Message, MessageSource> {

    protected abstract boolean isValid(Message message);

    protected abstract void acceptImpl(Message message, MessageSource messageSource);

    @Override
    public final void accept(Message message, MessageSource messageSource) {
        if (isValid(message)) {
            acceptImpl(message, messageSource);
        }
    }
}
