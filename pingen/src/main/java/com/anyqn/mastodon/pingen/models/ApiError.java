package com.anyqn.mastodon.pingen.models;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Value
@Jacksonized
@Builder
public class ApiError {
    long timestamp = Instant.now().toEpochMilli();
    int status;
    String error;
    String path;
    String message;
}
