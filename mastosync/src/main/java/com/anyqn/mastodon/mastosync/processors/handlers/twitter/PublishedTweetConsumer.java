package com.anyqn.mastodon.mastosync.processors.handlers.twitter;

import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.twitter.TweetsGroupResponse;
import com.anyqn.mastodon.mastosync.services.mastodon.MastodonPostsService;

import java.util.function.Consumer;

public class PublishedTweetConsumer implements Consumer<TweetsGroupResponse> {

    private final MastodonPostsService mastodonPostsService =
            SimpleContextFabric.IT.getInstance(MastodonPostsService.class);

    @Override
    public void accept(TweetsGroupResponse tweetsGroup) {
        mastodonPostsService.createReplyPosts(tweetsGroup);
    }
}
