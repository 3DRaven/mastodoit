package com.anyqn.mastodon.mastosync.services.mastodon;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccountId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonNotificationId;
import com.anyqn.mastodon.mastosync.enums.Event;
import com.anyqn.mastodon.mastosync.enums.MessageSource;
import com.anyqn.mastodon.mastosync.enums.Stream;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.Message;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.MentionPayload;
import com.anyqn.mastodon.mastosync.processors.handlers.mastodon.MessagesDelegatedHandler;
import com.anyqn.mastodon.mastosync.repostitories.local.LocalPostsRepository;
import com.anyqn.mastodon.mastosync.repostitories.mastodon.MastodonPostRepository;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Optional;

import static com.anyqn.mastodon.common.Constants.MASTODON_POSTS_PAGE_SIZE;
import static com.anyqn.mastodon.common.Constants.TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST;

public class MastodonAccountNotificationsFetchService {

    private final MastodonPostRepository mastodonPostRepository =
            SimpleContextFabric.IT.getInstance(MastodonPostRepository.class);
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);
    private final MastodonBucketService bucketService = SimpleContextFabric.IT.getInstance(MastodonBucketService.class);

    private final LocalPostsRepository localPostsRepository =
            SimpleContextFabric.IT.getInstance(LocalPostsRepository.class);

    public void readAllNotificationsFromLastSync(MastodonAccountId userId,
                                                 MessagesDelegatedHandler messagesDelegatedHandler) {
        @Nullable List<MentionPayload> notifications;
        do {
            if (bucketService.consumeNotificationReadingLimit(userId, MASTODON_POSTS_PAGE_SIZE)) {
                try {
                    Optional<MastodonNotificationId> sinceId = localPostsRepository.getLastFetchedMastodonNotificationId();
                    notifications =
                            mastodonPostRepository.getNotifications(configurationStateService.getMastodonHost(),
                                    sinceId.orElse(null));

                    notifications.stream()
                            .filter(it -> !localPostsRepository.isNotificationPublished(it.getId()))
                            .forEach(it -> {
                                Message message = Message.builder()
                                        .stream(List.of(Stream.USER))
                                        .event(Event.NOTIFICATION)
                                        .payload(it)
                                        .build();
                                messagesDelegatedHandler.handle(message, MessageSource.MASTODON_FETCH_NOTIFICATION);
                            });
                    bucketService.returnNotificationReadingLimit(
                            userId,
                            MASTODON_POSTS_PAGE_SIZE - notifications.size()
                    );
                } catch (Exception e) {
                    bucketService.returnNotificationReadingLimit(userId, TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST);
                    throw e;
                }
            } else {
                notifications = null;
            }
        } while (null != notifications && !notifications.isEmpty());
    }
}
