package com.anyqn.mastodon.mastosync.fabrics;

import lombok.SneakyThrows;

import java.util.concurrent.ConcurrentHashMap;

public enum SimpleContextFabric {
    IT;
    private final ConcurrentHashMap<Object, Object> instances = new ConcurrentHashMap<>();

    @SneakyThrows
    public <T> T getInstance(Class<T> clazz) {
        if (!instances.containsKey(clazz)) {
            synchronized (SimpleContextFabric.class) {
                if (!instances.containsKey(clazz)) {
                    //newInstance return non null value
                    instances.put(clazz, clazz.getDeclaredConstructor().newInstance());
                }
            }
        }
        return clazz.cast(instances.get(clazz));
    }
}
