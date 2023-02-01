package com.anyqn.mastodon.mastosync.fabrics;

import com.anyqn.mastodon.mastosync.processors.handlers.mastodon.DeletedStatusMessageConsumer;
import com.anyqn.mastodon.mastosync.processors.handlers.mastodon.CreatedStatusMessageConsumer;
import com.anyqn.mastodon.mastosync.processors.handlers.mastodon.CreatedNotificationMessageConsumer;
import com.anyqn.mastodon.mastosync.processors.handlers.mastodon.MessagesDelegatedHandler;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;

public enum MessagesDelegatedHandlerFabric implements ISingletonFabric<MessagesDelegatedHandler> {
    IT;

    private final MessagesDelegatedHandler messageRoutingController;
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);

    MessagesDelegatedHandlerFabric() {
        this.messageRoutingController = new MessagesDelegatedHandler();
        if (configurationStateService.isTwitterConfigured()) {
            messageRoutingController.addHandler(new CreatedNotificationMessageConsumer());
            messageRoutingController.addHandler(new CreatedStatusMessageConsumer());
            messageRoutingController.addHandler(new DeletedStatusMessageConsumer());
        }
    }

    @Override
    public MessagesDelegatedHandler getInstance() {
        return messageRoutingController;
    }
}
