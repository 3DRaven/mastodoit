package com.anyqn.mastodon.mastosync.services;

import org.jsoup.Jsoup;

public class HtmlProcessingService {

    public String clean(String html) {
        return Jsoup.parse(html).text();
    }
}
