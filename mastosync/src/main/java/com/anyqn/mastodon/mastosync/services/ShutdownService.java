package com.anyqn.mastodon.mastosync.services;

import com.anyqn.mastodon.common.util.Fluent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

@Slf4j
public class ShutdownService {

    private final Semaphore shutdownStartedSemaphore = new Semaphore(0);

    public CompletableFuture<Void> futureShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown detected");
            shutdownStartedSemaphore.release();
        }));

        return CompletableFuture.runAsync(() -> {
            Fluent.runAndRethrow(shutdownStartedSemaphore::acquire);
        });
    }
}
