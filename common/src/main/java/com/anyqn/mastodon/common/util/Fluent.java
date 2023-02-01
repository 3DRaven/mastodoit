package com.anyqn.mastodon.common.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

@Slf4j
public abstract class Fluent {
    @SneakyThrows
    public static void runAndRethrow(SilentRunnable silentRunnable) {
        try {
            silentRunnable.run();
        } catch (Exception exception) {
            log.error("Got error in call", exception);
            throw exception;
        }
    }

    @SneakyThrows
    public static <@NonNull T> @NonNull T returnAndRethrow(SilentSupplier<@NonNull T> silentRunnable) {
        try {
            return Objects.requireNonNull(silentRunnable.supply());
        } catch (Exception exception) {
            log.error("Got error in call", exception);
            throw exception;
        }
    }

    @FunctionalInterface
    public interface SilentRunnable {
        void run() throws Exception;
    }

    @FunctionalInterface
    public interface SilentSupplier<@NonNull T> {
        @NonNull T supply() throws Exception;
    }
}
