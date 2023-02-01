package com.anyqn.mastodon.mastosync.services.mastodon;

import com.anyqn.mastodon.common.util.OkHttpUtils;
import com.anyqn.mastodon.mastosync.controllers.AbstractSubscriptionEventListener;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.repostitories.ConfigurationFileRepository;
import com.anyqn.mastodon.mastosync.repostitories.mastodon.MastodonSubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

@Slf4j
public class MastodonSubscriptionService {

    private final MastodonSubscriptionRepository mastodonSubscriptionRepository =
            SimpleContextFabric.IT.getInstance(MastodonSubscriptionRepository.class);
    private final ConfigurationFileRepository configurationFileRepository =
            SimpleContextFabric.IT.getInstance(ConfigurationFileRepository.class);


    public void checkServerHealth() {
        log.info("Mastodon streaming health check");
        try (Response response = mastodonSubscriptionRepository.checkServerHealth(configurationFileRepository.getMastodonHost())) {
            if (!response.isSuccessful()) {
                throw new IllegalStateException(String.format("Streaming service on Mastodon server is down by " +
                        "reason [%s]", OkHttpUtils.getResponseBodyForLog(response)));
            } else {
                log.info("Streaming service on Mastodon server is [{}]", OkHttpUtils.getResponseBodyForLog(response));
            }
        }
    }

    public void subscribe(AbstractSubscriptionEventListener eventListener) {
        log.info("Subscription to Mastodon server");
        mastodonSubscriptionRepository.subscribe(configurationFileRepository.getMastodonHost(), eventListener);
    }

    public boolean isConnectionInProgress() {
        return mastodonSubscriptionRepository.isConnectionInProgress();
    }


    public void disconnect() {
        if (mastodonSubscriptionRepository.isConnectionInProgress()) {
            log.info("Disconnection from Mastodon server");
            mastodonSubscriptionRepository.disconnect();
        }
    }
}
