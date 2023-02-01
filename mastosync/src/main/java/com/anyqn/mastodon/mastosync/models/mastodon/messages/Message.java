package com.anyqn.mastodon.mastosync.models.mastodon.messages;

import com.anyqn.mastodon.mastosync.enums.Event;
import com.anyqn.mastodon.mastosync.enums.Stream;
import com.anyqn.mastodon.mastosync.models.deserializers.MessageDeserializer;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.AbstractPayload;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(using = MessageDeserializer.class)
public class Message {
    @Builder.Default
    List<Stream> stream = List.of();
    Event event;
    AbstractPayload payload;
}
