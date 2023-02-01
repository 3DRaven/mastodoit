package com.anyqn.mastodon.mastosync.transformers.to.twitter;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonNotificationId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.mastosync.fabrics.SimpleContextFabric;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.AbstractPayload;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.MentionPayload;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.PostDeletedPayload;
import com.anyqn.mastodon.mastosync.models.mastodon.messages.payloads.StatusPayload;
import com.anyqn.mastodon.mastosync.models.twitter.FutureTweet;
import com.anyqn.mastodon.mastosync.models.twitter.ReplyToTweet;
import com.anyqn.mastodon.mastosync.processors.visitors.IMastodonPayloadVisitor;
import com.anyqn.mastodon.mastosync.services.HtmlProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Optional;

import static com.anyqn.mastodon.common.Constants.TWEET_MAX_TEXT_LENGTH;
import static com.anyqn.mastodon.common.Constants.TWEET_SHORTEN_LINK_SIZE;

@Slf4j
public class MastodonPostToTweetTransformer implements IMastodonPayloadVisitor {
    private final TemplateEngine templateEngine;
    private final HtmlProcessingService htmlProcessingService =
            SimpleContextFabric.IT.getInstance(HtmlProcessingService.class);

    public MastodonPostToTweetTransformer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public FutureTweet map(boolean eventFromMainUser, AbstractPayload payload, @Nullable TweetId parentTweetId) {
        return payload.visit(eventFromMainUser, parentTweetId, this);
    }

    @Override
    public FutureTweet visit(boolean eventFromMainUser, @Nullable TweetId parentTweetId, MentionPayload payload) {
        return getFutureTweet(eventFromMainUser, (MastodonNotificationId) payload.getId(), parentTweetId, payload.getStatus());
    }

    @Override
    public FutureTweet visit(boolean eventFromMainUser, @Nullable TweetId parentTweetId, PostDeletedPayload payload) {
        throw new IllegalStateException("Conversion from post deleted payload to tweet is not implemented");
    }

    @Override
    public FutureTweet visit(boolean eventFromMainUser, @Nullable TweetId parentTweetId, StatusPayload payload) {
        return getFutureTweet(eventFromMainUser, null, parentTweetId, payload);
    }

    private FutureTweet getFutureTweet(boolean eventFromMainUser,
                                       @Nullable MastodonNotificationId mastodonNotificationId,
                                       @Nullable TweetId parentTweetId,
                                       StatusPayload payload) {
        Context context = new Context();
        if (eventFromMainUser) {
            //Support text length calculation
            context.setVariable("text", "");
            context.setVariable("mastodonReplyLink", "");
            context.setVariable("cleanTextLength", TWEET_MAX_TEXT_LENGTH);
            String onlyTemplateText = templateEngine.process("tweet", context);
            //TODO: Сделать параметрическую обработку построения твитов. Спрашивать раз в день длинну короткой ссылки, в
            // твите Reply вынести в проперти и длинну его считать, не пользоваться константами
            context.setVariable("text", htmlProcessingService.clean(payload.getContent()));
            context.setVariable("mastodonReplyLink", payload.getUrl());
            //https://developer.twitter.com/en/docs/tco
            context.setVariable("cleanTextLength",
                    TWEET_MAX_TEXT_LENGTH - onlyTemplateText.length() - TWEET_SHORTEN_LINK_SIZE);
            return buildFutureTweet(payload, mastodonNotificationId, parentTweetId, context, "tweet");
        } else {
            context.setVariable("mastodonReplyLink", payload.getUrl());
            context.setVariable("extUserName", payload.getAccount().getUsername());
            return buildFutureTweet(payload, mastodonNotificationId, parentTweetId, context, "ext-tweet");
        }
    }

    private FutureTweet buildFutureTweet(StatusPayload statusPayload,
                                         @Nullable MastodonNotificationId mastodonNotificationId,
                                         @Nullable TweetId parentTweetId,
                                         Context context,
                                         String templateName) {
        return FutureTweet.builder()
                .reply(Optional.ofNullable(parentTweetId).map(it -> ReplyToTweet.builder()
                                .inReplyToTweetId(it)
                                .build())
                        .orElse(null))
                .text(templateEngine.process(templateName, context))
                .replyUrl(statusPayload.getUrl())
                .mastodonStatusId((MastodonStatusId) statusPayload.getId())
                .mastodonNotificationId(mastodonNotificationId)
                .sourceStatus(statusPayload)
                .build();
    }
}
