package com.anyqn.mastodon.mastosync.controllers;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonHost;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.common.util.OkHttpUtils;
import com.anyqn.mastodon.mastosync.enums.MessageSource;
import com.anyqn.mastodon.mastosync.fabrics.MessagesDelegatedHandlerFabric;
import com.anyqn.mastodon.mastosync.fabrics.ObjectMapperFabric;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.ErrorListenerFinalStatus;
import com.anyqn.mastodon.mastosync.models.mastodon.ListenerFinalStatus;
import com.anyqn.mastodon.mastosync.models.mastodon.NormalListenerFinalStatus;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.Message;
import com.anyqn.mastodon.mastosync.processors.handlers.mastodon.MessagesDelegatedHandler;
import com.anyqn.mastodon.mastosync.processors.visitors.IFinalStatusVisitor;
import com.anyqn.mastodon.mastosync.services.mastodon.MastodonSubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

@Slf4j
public class MastodonStreamSubscriptionController extends AbstractSubscriptionEventListener implements IFinalStatusVisitor {

    private final SynchronousQueue<ListenerFinalStatus> finalStatusProducer = new SynchronousQueue<>();
    private final MastodonSubscriptionService mastodonSubscriptionService =
            SimpleContextFabric.IT.getInstance(MastodonSubscriptionService.class);
    private final ObjectMapper objectMapper = ObjectMapperFabric.IT.getInstance();
    private final MessagesDelegatedHandler messagesDelegatedHandler =
            MessagesDelegatedHandlerFabric.IT.getInstance();

    public synchronized void subscribeToMainUserTimeline() {
        if (!mastodonSubscriptionService.isConnectionInProgress()) {
            mastodonSubscriptionService.checkServerHealth();
            mastodonSubscriptionService.subscribe(this);
        } else {
            mastodonSubscriptionService.disconnect();
            throw new IllegalStateException("Connection already in progress and was disconnected");
        }
    }

    public void waitFinish(Semaphore gracefullyShutdownFinishedSemaphore) {
        if (mastodonSubscriptionService.isConnectionInProgress()) {
            futureDisconnect(this, gracefullyShutdownFinishedSemaphore).join();
            log.info("Gracefully shutdown");
            mastodonSubscriptionService.disconnect();
        } else {
            throw new IllegalStateException("Client is disconnected");
        }
    }

    public CompletableFuture<Void> futureDisconnect(IFinalStatusVisitor visitor, Semaphore gracefullyShutdownFinishedSemaphore) {
        return CompletableFuture.runAsync(() ->
                Fluent.runAndRethrow(() -> finalStatusProducer.take().visit(visitor, gracefullyShutdownFinishedSemaphore)));
    }

    //Collection of methods for events processing
    //ATTENTION: all methods will be called from different threads
    @Override
    public void visit(NormalListenerFinalStatus status, Semaphore gracefullyShutdownFinishedSemaphore) {
        try {
            log.info("Websocket to the Mastodon server was closed normally with code [{}] and reason [{}]",
                    status.getCode(),
                    status.getReason());
        } finally {
            //Allow to close for shutdown hack
            gracefullyShutdownFinishedSemaphore.release();
        }
    }

    @Override
    public void visit(ErrorListenerFinalStatus status, Semaphore gracefullyShutdownFinishedSemaphore) {
        try {
            log.info("Websocket to the Mastodon server was closed exceptionally with throwable [{}] and response [{}]",
                    ExceptionUtils.getRootCauseMessage(status.getError()),
                    status.getResponse());
        } finally {
            //Allow to close for shutdown hack
            gracefullyShutdownFinishedSemaphore.release();
        }
    }

    @Override
    public void onConnectionError(Throwable throwable) {
        log.debug("Connection error: [{}]", ExceptionUtils.getRootCauseMessage(throwable));
        Fluent.runAndRethrow(() -> finalStatusProducer.put(new ErrorListenerFinalStatus(throwable, null)));
    }

    @Override
    public void onConnectionStarting(MastodonHost mastodonHost) {
        log.info("Connection to host [{}]", mastodonHost);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        log.debug("Connection to server with response: [{}]", OkHttpUtils.getResponseBodyForLog(response));
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        messagesDelegatedHandler.handle(Fluent.returnAndRethrow(() -> objectMapper.readValue(message, Message.class)),
                MessageSource.MASTODON_USER_STREAM);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        log.debug("Byte message: [{}]", bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        log.debug("Connection to Mastodon server closed");
        Fluent.runAndRethrow(() -> finalStatusProducer.put(new NormalListenerFinalStatus(code, reason)));
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable throwable, @Nullable Response response) {
        log.debug("Connection failure with error [{}]", ExceptionUtils.getRootCauseMessage(throwable));
//        Fluent.runAndRethrow(() -> finalStatusProducer.put(new ErrorListenerFinalStatus(throwable,
//                Optional.ofNullable(response).map(OkHttpUtils::getResponseBodyForLog).orElse(null))));
    }
}
