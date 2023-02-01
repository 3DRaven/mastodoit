package com.anyqn.mastodon.mastosync.fabrics;

import com.anyqn.mastodon.mastosync.transformers.to.twitter.MastodonPostToTweetTransformer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.nio.charset.StandardCharsets;

public enum MastodonPostToTweetTransformerFabric implements ISingletonFabric<MastodonPostToTweetTransformer> {
    IT;

    private final MastodonPostToTweetTransformer instance;

    MastodonPostToTweetTransformerFabric() {
        TemplateEngine templateEngine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setPrefix("templates/");
        resolver.setSuffix(".txt");
        templateEngine.setTemplateResolver(resolver);
        instance = new MastodonPostToTweetTransformer(templateEngine);
    }

    public MastodonPostToTweetTransformer getInstance() {
        return instance;
    }
}
