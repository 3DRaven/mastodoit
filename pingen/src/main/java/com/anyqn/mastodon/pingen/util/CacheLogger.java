package com.anyqn.mastodon.pingen.util;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

import java.util.Optional;

@Slf4j
public class CacheLogger implements CacheEventListener<Object, Object> {
    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        log.info("Key: {} | EventType: {} | Old value: {} | New value: {}",
                Optional.ofNullable(cacheEvent.getKey()),
                Optional.ofNullable(cacheEvent.getType()),
                Optional.ofNullable(cacheEvent.getOldValue()),
                Optional.ofNullable(cacheEvent.getNewValue()));
    }
}