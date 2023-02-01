package com.anyqn.mastodon.pingen.controllers;

import com.anyqn.mastodon.common.models.twitter.TwitterAuthHeaderResponse;
import com.anyqn.mastodon.common.models.twitter.TwitterAuthUrlResponse;
import com.anyqn.mastodon.common.models.twitter.TwitterCallDescriptor;
import com.anyqn.mastodon.common.models.twitter.TwitterRequestData;
import com.anyqn.mastodon.common.models.values.twitter.TwitterConsumerKey;
import com.anyqn.mastodon.common.models.values.twitter.TwitterConsumerSecret;
import com.anyqn.mastodon.pingen.services.twitter.TwitterUserAuthService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TwitterAuthRequestsController {

    private final TwitterUserAuthService twitterUserAuthService;
    private final TwitterConsumerKey consumerKey;
    private final TwitterConsumerSecret consumerSecret;

    public TwitterAuthRequestsController(TwitterUserAuthService twitterUserAuthService,
                                         @Value("${client.key}") TwitterConsumerKey consumerKey,
                                         @Value("${client.secret}") TwitterConsumerSecret consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.twitterUserAuthService = twitterUserAuthService;
    }

    @GetMapping("/api/v1/auth/url/twitter")
    @ResponseBody
    @SneakyThrows
    TwitterAuthUrlResponse getTwitterAuthUrl() {
        return twitterUserAuthService.getAuthUrl(consumerKey,consumerSecret);
    }

    @RequestMapping(path = "/api/v1/auth/token/twitter", method = POST, consumes =
            {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @SneakyThrows
    TwitterAuthHeaderResponse getTwitterAuthHeader(@RequestBody TwitterCallDescriptor twitterCallDescriptor) {
        return twitterUserAuthService.getAuthHeader(new TwitterRequestData(
                consumerKey,
                consumerSecret,
                twitterCallDescriptor
        ));
    }
}
