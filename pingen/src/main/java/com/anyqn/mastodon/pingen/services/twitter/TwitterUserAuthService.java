package com.anyqn.mastodon.pingen.services.twitter;

import com.anyqn.mastodon.common.models.twitter.TwitterAuthHeaderResponse;
import com.anyqn.mastodon.common.models.twitter.TwitterAuthUrlResponse;
import com.anyqn.mastodon.common.models.twitter.TwitterRequestData;
import com.anyqn.mastodon.common.models.values.twitter.TwitterConsumerKey;
import com.anyqn.mastodon.common.models.values.twitter.TwitterConsumerSecret;
import com.anyqn.mastodon.pingen.repository.twitter.TwitterAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class TwitterUserAuthService {

    private final TwitterAuthRepository twitterAuthRepository;

    public TwitterAuthUrlResponse getAuthUrl(TwitterConsumerKey consumerKey, TwitterConsumerSecret consumerSecret) {
        return twitterAuthRepository.getTwitterAuthUrl(consumerKey,consumerSecret);
    }

    public TwitterAuthHeaderResponse getAuthHeader( TwitterRequestData twitterRequestData) {
        return TwitterAuthHeaderResponse.builder().header(twitterRequestData.getAuthorizationHeader()).build();
    }
}
