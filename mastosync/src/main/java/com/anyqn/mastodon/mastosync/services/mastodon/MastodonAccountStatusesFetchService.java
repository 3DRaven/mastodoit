package com.anyqn.mastodon.mastosync.services.mastodon;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonAccountId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.mastosync.enums.Event;
import com.anyqn.mastodon.mastosync.enums.MessageSource;
import com.anyqn.mastodon.mastosync.enums.Stream;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.Message;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.StatusPayload;
import com.anyqn.mastodon.mastosync.processors.handlers.mastodon.MessagesDelegatedHandler;
import com.anyqn.mastodon.mastosync.repostitories.local.LocalPostsRepository;
import com.anyqn.mastodon.mastosync.repostitories.mastodon.MastodonPostRepository;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Optional;

import static com.anyqn.mastodon.common.Constants.MASTODON_POSTS_PAGE_SIZE;
import static com.anyqn.mastodon.common.Constants.TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST;

public class MastodonAccountStatusesFetchService {

    private final MastodonPostRepository mastodonPostRepository =
            SimpleContextFabric.IT.getInstance(MastodonPostRepository.class);
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);
    private final MastodonBucketService bucketService = SimpleContextFabric.IT.getInstance(MastodonBucketService.class);

    private final LocalPostsRepository localPostsRepository =
            SimpleContextFabric.IT.getInstance(LocalPostsRepository.class);

    public void readAllStatusesFromLastSync(MastodonAccountId userId,
                                            MessagesDelegatedHandler messagesDelegatedHandler) {
        @Nullable List<StatusPayload> statuses;
        do {
            if (bucketService.consumeStatusesReadingLimit(userId, MASTODON_POSTS_PAGE_SIZE)) {
                try {
                    Optional<MastodonStatusId> sinceId = localPostsRepository.getLastFetchedMastodonStatusId();

                    statuses = mastodonPostRepository.getAccountStatuses(configurationStateService.getMastodonHost(),
                            userId,
                            sinceId.orElse(null));

                    statuses.stream()
                            .filter(it -> !localPostsRepository.isPostPublished((MastodonStatusId) it.getId()))
                            .forEach(it ->
                                    messagesDelegatedHandler.handle(Message.builder()
                                                    .event(Event.UPDATE)
                                                    .stream(List.of(Stream.USER))
                                                    .payload(it)
                                                    .build(),
                                            MessageSource.MASTODON_FETCH_STATUSES)
                            );

                    bucketService.consumeStatusesReadingLimit(
                            userId,
                            MASTODON_POSTS_PAGE_SIZE - statuses.size()
                    );
                } catch (Exception e) {
                    bucketService.consumeStatusesReadingLimit(userId, TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST);
                    throw e;
                }
            } else {
                statuses = null;
            }
        } while (null != statuses && !statuses.isEmpty());
    }
}
