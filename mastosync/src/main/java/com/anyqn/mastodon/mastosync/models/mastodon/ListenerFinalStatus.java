package com.anyqn.mastodon.mastosync.models.mastodon;

import com.anyqn.mastodon.mastosync.processors.visitors.IFinalStatusVisitor;

import java.util.concurrent.Semaphore;

public abstract class ListenerFinalStatus {
    public abstract void visit(IFinalStatusVisitor visitor, Semaphore gracefullyShutdownFinishedSemaphore);
}
