package com.anyqn.mastodon.mastosync.services;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonAuthorizationCode;
import com.anyqn.mastodon.common.models.values.twitter.TwitterPinCode;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.repostitories.UserInputRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class UserInputService {

    private final UserInputRepository userInputRepository =
            SimpleContextFabric.IT.getInstance(UserInputRepository.class);

    public MastodonAuthorizationCode getMastodonCode() {
        return new MastodonAuthorizationCode(userInputRepository.getNextLine());
    }

    public TwitterPinCode getTwitterPin() {
        return new TwitterPinCode(userInputRepository.getNextLine());
    }

    public CompletableFuture<Void> futurePressEnter() {
        return CompletableFuture.runAsync(() -> {
            log.info("\u001B[31m" + "Listening of posts in progress, for exit press Enter twice" + "\u001B[0m");
            userInputRepository.getNextLine();
        });
    }
}
