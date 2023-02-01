package com.anyqn.mastodon.pingen.controllers;

import com.anyqn.mastodon.common.models.twitter.tweets.TweetsGroup;
import com.anyqn.mastodon.common.models.values.twitter.*;
import com.anyqn.mastodon.pingen.services.twitter.TwitterFetchTweetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import static com.anyqn.mastodon.common.Constants.*;

@RestController
@RequiredArgsConstructor
public class TwitterTweetsRequestsController {
    private final TwitterFetchTweetsService twitterTweetsFetchService;

    @GetMapping(path = "/api/v1/twitter/users/{tweeterUserId}/tweets")
    @ResponseBody
    TweetsGroup getTwitterAuthHeader(@PathVariable TwitterUserId tweeterUserId,
                                     @Nullable @RequestParam(name = PINGEN_TWITTER_SINCE_ID_EXCLUDED_PARAM) TweetId sinceId,
                                     @Nullable @RequestParam(name = PINGEN_TWITTER_UNTIL_ID_EXCLUDED_PARAM) TweetId untilId,
                                     @Nullable @RequestParam(name = PINGEN_TWITTER_PAGE_TOKEN_PARAM) TwitterPaginationToken twitterPaginationToken,
                                     @RequestHeader(PINGEN_TWITTER_ACCESS_TOKEN_HEADER) TwitterUserAccessToken accessToken,
                                     @RequestHeader(PINGEN_TWITTER_ACCESS_TOKEN_SECRET_HEADER) TwitterUserAccessTokenSecret accessTokenSecret) {
        return twitterTweetsFetchService.getTweets(tweeterUserId, sinceId, untilId, twitterPaginationToken, accessToken,
                accessTokenSecret);
    }
}