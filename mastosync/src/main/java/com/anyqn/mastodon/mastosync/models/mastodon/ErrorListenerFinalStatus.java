package com.anyqn.mastodon.mastosync.models.mastodon;

import com.anyqn.mastodon.mastosync.processors.visitors.IFinalStatusVisitor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.concurrent.Semaphore;

@Value
@EqualsAndHashCode(callSuper = false)
public class ErrorListenerFinalStatus extends ListenerFinalStatus {
    Throwable error;
    @Nullable
    String response;

    public Optional<String> getResponse() {
        return Optional.ofNullable(response);
    }

    @Override
    public void visit(IFinalStatusVisitor visitor, Semaphore gracefullyShutdownFinishedSemaphore) {
        visitor.visit(this, gracefullyShutdownFinishedSemaphore);
    }
}
