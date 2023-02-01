package com.anyqn.mastodon.common.models.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import okhttp3.HttpUrl;

import java.io.IOException;

public class HttpUrlDeserialize extends StdDeserializer<HttpUrl> {
    protected HttpUrlDeserialize() {
        super(HttpUrl.class);
    }

    @Override
    public HttpUrl deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);
        return HttpUrl.get(node.asText());
    }
}
