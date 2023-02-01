package com.anyqn.mastodon.mastosync.controllers;

import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import com.anyqn.mastodon.mastosync.services.ShutdownService;
import com.anyqn.mastodon.mastosync.services.UserInputService;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExitWaiterController {
    private final UserInputService userInputService =
            SimpleContextFabric.IT.getInstance(UserInputService.class);
    private final ShutdownService shutdownService =
            SimpleContextFabric.IT.getInstance(ShutdownService.class);
    private static final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);


    public void waitFinish(ExecutorService... fetchStatusesAndNotifications) {
        try {
            CompletableFuture<Void> keyTask = userInputService.futurePressEnter();
            CompletableFuture<Void> shutDownTask = shutdownService.futureShutdown();
            CompletableFuture.anyOf(keyTask, shutDownTask).join();
            log.info("Gracefully shutdown");
        } catch (Throwable throwable) {
            log.info("Finalization");
            throw throwable;
        } finally {
            waitGracefullyShutdown(fetchStatusesAndNotifications);
        }
    }

    private static void waitGracefullyShutdown(ExecutorService... fetchStatusesAndNotifications) {
        CompletableFuture.allOf(
                        Arrays.stream(fetchStatusesAndNotifications).map(it -> {
                            it.shutdown();
                            return CompletableFuture.runAsync(() -> {
                                if (!Fluent.returnAndRethrow(() -> it.awaitTermination(configurationStateService.getFetcherShutdownTimoutMillis(), TimeUnit.MILLISECONDS))) {
                                    log.warn("Gracefully shutdown canceled for executor");
                                }
                            });
                        }).toArray(CompletableFuture[]::new))
                .join();
    }
}
