package com.anyqn.mastodon.mastosync.processors.handlers.mastodon;

import com.anyqn.mastodon.mastosync.enums.MessageSource;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Slf4j
public class MessagesDelegatedHandler {

    private final List<BaseMessageConsumer> postCreatedListeners = new ArrayList<>();

    public void addHandler(BaseMessageConsumer listener) {
        postCreatedListeners.add(listener);
    }

    public void handle(Message message, MessageSource messageSource) {
        //TODO: here must be queue of messages because handlers processing may be slow and next messages can be
        // depend on previous unprocessed messages
        log.debug("Received message [{}]", message);
        for (BiConsumer<Message, MessageSource> payloadHandler : postCreatedListeners) {
            //We will have few types of listeners, so we do not really need any executor services or other
            CompletableFuture.runAsync(() -> {
                payloadHandler.accept(message, messageSource);
            }).handle((result, throwable) -> {
                if (null != throwable) {
                    log.error("Error in mastodon posts listener [{}]",
                            Optional.ofNullable(ExceptionUtils.getRootCause(throwable))
                                    .map(Throwable::toString)
                                    .orElse(ExceptionUtils.getRootCauseMessage(throwable)));
                    return CompletableFuture.failedFuture(throwable);
                } else {
                    //For nullability checker :)
                    return CompletableFuture.completedFuture(new Object());
                }
            });
        }
    }
}
