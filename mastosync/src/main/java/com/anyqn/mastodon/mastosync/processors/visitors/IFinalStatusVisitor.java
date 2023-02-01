package com.anyqn.mastodon.mastosync.processors.visitors;

import com.anyqn.mastodon.mastosync.models.mastodon.ErrorListenerFinalStatus;
import com.anyqn.mastodon.mastosync.models.mastodon.NormalListenerFinalStatus;

import java.util.concurrent.Semaphore;

public interface IFinalStatusVisitor {
    void visit(NormalListenerFinalStatus status, Semaphore gracefullyShutdownFinishedSemaphore);

    void visit(ErrorListenerFinalStatus status, Semaphore gracefullyShutdownFinishedSemaphore);
}