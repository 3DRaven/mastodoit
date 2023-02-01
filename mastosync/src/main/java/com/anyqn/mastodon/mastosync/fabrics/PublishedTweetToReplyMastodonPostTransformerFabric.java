package com.anyqn.mastodon.mastosync.fabrics;

import com.anyqn.mastodon.mastosync.transformers.to.twitter.PublishedTweetToMastodonReplyPostTransformer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.nio.charset.StandardCharsets;

public enum PublishedTweetToReplyMastodonPostTransformerFabric implements ISingletonFabric<PublishedTweetToMastodonReplyPostTransformer> {
    IT;

    private final PublishedTweetToMastodonReplyPostTransformer instance;

    PublishedTweetToReplyMastodonPostTransformerFabric() {
        TemplateEngine templateEngine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setPrefix("templates/");
        resolver.setSuffix(".txt");
        templateEngine.setTemplateResolver(resolver);
        instance = new PublishedTweetToMastodonReplyPostTransformer(templateEngine);
    }

    public PublishedTweetToMastodonReplyPostTransformer getInstance() {
        return instance;
    }
}
