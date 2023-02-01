package com.anyqn.mastodon.mastosync.models.deserializers;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.mastosync.enums.Event;
import com.anyqn.mastodon.mastosync.enums.Stream;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.Message;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.AbstractPayload;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.MentionPayload;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.PostDeletedPayload;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.StatusPayload;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class MessageDeserializer extends StdDeserializer<Message> {

    private final ObjectMapper mapper =
            JsonMapper.builder()
                    .findAndAddModules()
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).build();

    public MessageDeserializer() {
        super(Message.class);
    }

    @Override
    public Message deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);

        List<Stream> stream;
        try (JsonParser parser = node.get("stream").traverse(codec)) {
            stream = parser.readValueAs(new TypeReference<List<Stream>>() {
            });
        }

        String eventText = node.get("event").asText().toUpperCase();
        Event payloadType;
        try {
            payloadType = Event.valueOf(eventText);
        } catch (IllegalArgumentException e) {
            log.error("Unsupported event type [{}]", eventText);
            throw e;
        }

        AbstractPayload abstractPayload;
        switch (payloadType) {
            case UPDATE:
                abstractPayload = mapper.readValue(node.get("payload").asText(), StatusPayload.class);
                break;
            case DELETE:
                abstractPayload = PostDeletedPayload.builder().id(new MastodonStatusId(node.get("payload").asText())).build();
                break;
            case NOTIFICATION:
                abstractPayload = mapper.readValue(node.get("payload").asText(), MentionPayload.class);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported payload type %s", payloadType));
        }
        return Message.builder().event(payloadType).payload(abstractPayload).stream(Optional.ofNullable(stream).orElse(List.of())).build();
    }
}
