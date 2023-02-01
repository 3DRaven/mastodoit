package com.anyqn.mastodon.mastosync.models.twitter;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class TwitterAuthHeaderResponse {
    String header;
    @Override
    public String toString() {
        return "TwitterAuthHeaderResponse{ header='***'}";
    }
}