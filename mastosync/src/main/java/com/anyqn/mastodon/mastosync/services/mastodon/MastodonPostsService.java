package com.anyqn.mastodon.mastosync.services.mastodon;

import com.anyqn.mastodon.common.models.twitter.tweets.PublishedTweet;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.mastosync.enums.MessageSource;
import com.anyqn.mastodon.mastosync.fabrics.PublishedTweetToReplyMastodonPostTransformerFabric;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.twitter.TweetsGroupResponse;
import com.anyqn.mastodon.mastosync.repostitories.local.LocalPostsRepository;
import com.anyqn.mastodon.mastosync.repostitories.local.LocalTweetsRepository;
import com.anyqn.mastodon.mastosync.repostitories.mastodon.MastodonPostRepository;
import com.anyqn.mastodon.mastosync.services.ConfigurationStateService;
import com.anyqn.mastodon.mastosync.transformers.to.twitter.PublishedTweetToMastodonReplyPostTransformer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MastodonPostsService {
    private final MastodonPostRepository mastodonPostRepository =
            SimpleContextFabric.IT.getInstance(MastodonPostRepository.class);
    private final LocalTweetsRepository localTweetsRepository =
            SimpleContextFabric.IT.getInstance(LocalTweetsRepository.class);
    private final ConfigurationStateService configurationStateService =
            SimpleContextFabric.IT.getInstance(ConfigurationStateService.class);

    private final PublishedTweetToMastodonReplyPostTransformer publishedTweetToReplyMastodonPostTransformerFabric =
            PublishedTweetToReplyMastodonPostTransformerFabric.IT.getInstance();

    private final LocalPostsRepository localPostsRepository =
            SimpleContextFabric.IT.getInstance(LocalPostsRepository.class);

    final Pattern regex = Pattern.compile("^Tweet id:(.*)$", Pattern.MULTILINE);

    /**
     * If it is main user reply to post published in user sync tree (conversation started by main user and saved to
     * local sync db) we will get associated tweet id from local db, but if it is conversation published by another user
     * we need get tweet id from post if it is possible
     *
     * @param mastodonStatusId post id which main user replayed
     * @param content          text content of post
     * @return associated to replayed post tweet, published in tweeter
     */
    public Optional<TweetId> getAssociatedTweetId(MastodonStatusId mastodonStatusId, @Nullable String content) {
        return localPostsRepository.getAssociatedTweetId(mastodonStatusId).or(() -> {
            if (null != content) {
                //TODO: заменить в шаблонах твит регексп на константу что бы редактировать ее в шаблоне было нельзя
                final Matcher matcher = regex.matcher(content);
                if (matcher.matches()) {
                    return Optional.ofNullable(matcher.group(1)).map(TweetId::new);
                }
            }
            return Optional.empty();
        });
    }

    public void createReplyPosts(TweetsGroupResponse tweetsGroupResponse) {
        tweetsGroupResponse.getTweetsGroup()
                .getData()
                .stream()
                //Sorted because we need process tweets from parents to children
                .sorted(Comparator.comparing(PublishedTweet::getId))
                .forEach(publishedTweet -> {
                    //We process only reply tweets from conversations published in Mastodon
                    if (null != publishedTweet.getConversationId() &&
                            localTweetsRepository.isTweetExists(Objects.requireNonNull(publishedTweet.getConversationId()))) {
                        //We process only tweets which parent has published mastodon post
                        //It may be root post from mastodon user or reply posts from reply mastodon user
                        //If it is retweet or quote tweet, we will publish it just as regular reply post
                        publishedTweet.getParentId()
                                .flatMap(localTweetsRepository::getAssociatedMastodonId)
                                .ifPresent(parentMastodonPostId -> {
                                    localTweetsRepository.savePublishedTweet(publishedTweet,
                                            mastodonPostRepository.createReplyPost(
                                                            configurationStateService.getMastodonHost(),
                                                            publishedTweetToReplyMastodonPostTransformerFabric.map(
                                                                    publishedTweet,
                                                                    parentMastodonPostId))
                                                    .getId(),
                                            null,//It is not fetched notification from mastodon
                                            MessageSource.TWITTER_REPLY);
                                });
                    }
                });
    }
}