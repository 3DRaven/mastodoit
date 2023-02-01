package com.anyqn.mastodon.mastosync.fabrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public enum ObjectMapperFabric implements ISingletonFabric<ObjectMapper> {
    IT;

    private final ObjectMapper mapper = JsonMapper.builder()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .findAndAddModules()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .build()
            .findAndRegisterModules();

    @Override
    public ObjectMapper getInstance() {
        return mapper;
    }
}
