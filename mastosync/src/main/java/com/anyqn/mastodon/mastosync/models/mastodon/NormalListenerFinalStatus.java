package com.anyqn.mastodon.mastosync.models.mastodon;

import com.anyqn.mastodon.mastosync.processors.visitors.IFinalStatusVisitor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.concurrent.Semaphore;

@Value
@EqualsAndHashCode(callSuper = false)
public class NormalListenerFinalStatus extends ListenerFinalStatus {
    int code;
    String reason;

    @Override
    public void visit(IFinalStatusVisitor visitor, Semaphore gracefullyShutdownFinishedSemaphore) {
        visitor.visit(this,gracefullyShutdownFinishedSemaphore);
    }
}
