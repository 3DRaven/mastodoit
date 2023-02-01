package com.anyqn.mastodon.mastosync.transformers.to.twitter;

import com.anyqn.mastodon.common.models.twitter.tweets.PublishedTweet;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.mastosync.models.mastodon.FutureReplyMastodonPost;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static com.anyqn.mastodon.common.Constants.POST_MAX_TEXT_LENGTH;

@Slf4j
public class PublishedTweetToMastodonReplyPostTransformer {
    private final TemplateEngine templateEngine;

    public PublishedTweetToMastodonReplyPostTransformer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public FutureReplyMastodonPost map(PublishedTweet tweet, MastodonStatusId parentPostId) {
        Context context = new Context();
        context.setVariable("text", tweet.getText());
        context.setVariable("tweetId", tweet.getId().getValue());
        context.setVariable("cleanTextLength", POST_MAX_TEXT_LENGTH);
        String onlyTemplateText = templateEngine.process("post", context);
        context.setVariable("cleanTextLength", POST_MAX_TEXT_LENGTH - onlyTemplateText.length());
        return FutureReplyMastodonPost.builder().status(templateEngine.process("post", context))
                .inReplyToId(parentPostId)
                .sensitive(tweet.isPossiblySensitive())
                .idempotencyKey(tweet.getId().getValue())
                .build();
    }
}
