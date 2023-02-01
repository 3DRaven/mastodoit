package com.anyqn.mastodon.common;

import okhttp3.HttpUrl;
import okhttp3.MediaType;

import java.util.Objects;

import static org.checkerframework.checker.nullness.util.NullnessUtil.castNonNull;

public abstract class Constants {
    public static final int MAX_SQL_CACHE_SIZE = 250;
    public static final int NORMAL_EXIT_RESPONSE_CODE = 1000;
    public static final int TWEET_ALLOWED_MESSAGE_LENGTH_SYMBOLS = 221;
    public static final int TWEET_SHORTEN_LINK_SIZE = 23;
    public static final int TWEET_REPLY_TEXT_LENGTH_SYMBOLS = 6;
    public static final int TWEET_MAX_TEXT_LENGTH = 250;
    public static final int POST_MAX_TEXT_LENGTH = 500;
    public static final String MASTODON_OAUTH_TOKEN_PREFIX = "Bearer ";
    public static final String DEFAULT_JDBC_URL = "jdbc:sqlite:local.db";
    public static final int DEFAULT_POLLING_TIME_MILLIS = 1 /*min*/ * 10 /*sec*/ * 1000 /*millis*/;

    public static final String TWITTER_MAX_RESULTS_QUERY_PARAM_NAME = "max_results";
    public static final String TWITTER_PAGINATION_TOKEN = "pagination_token";
    public static final String TWITTER_SINCE_ID = "since_id";
    public static final String TWITTER_UNTIL_ID = "until_id";
    public static final HttpUrl TWITTER_BASE_URL = Objects.requireNonNull(HttpUrl.get("https://api.twitter.com"));
    public static final String TWITTER_QUERY_OAUTH_CALLBACK = "oauth_callback";
    public static final String TWITTER_QUERY_OAUTH_TOKEN = "oauth_token";
    public static final String TWITTER_CALLBACK_TYPE = "oob";
    public static final String TWITTER_FINAL_URL_BASE = "https://api.twitter.com/oauth/authorize?oauth_token=";
    public static final String TWITTER_RESPONSE_OAUTH_TOKEN_FIELD = "oauth_token";
    public static final String TWITTER_RESPONSE_OAUTH_TOKEN_SECRET_FIELD = "oauth_token_secret";
    public static final String TWITTER_RESPONSE_OAUTH_CALLBACK_CONFIRMED_FIELD = "oauth_callback_confirmed";
    public static final String TWITTER_SIGNATURE_ALGORITHM = "HMAC-SHA1";
    public static final String TWITTER_QUERY_OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    public static final String TWITTER_QUERY_OAUTH_NONCE = "oauth_nonce";
    public static final String TWITTER_QUERY_OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    public static final String TWITTER_QUERY_OAUTH_TIMESTAMP = "oauth_timestamp";
    public static final String TWITTER_QUERY_OAUTH_VERSION = "oauth_version";
    public static final String TWITTER_QUERY_OAUTH_SIGNATURE = "oauth_signature";
    public static final String TWITTER_OAUTH_VERSION = "1.0";
    public static final int TWITTER_NONCE_SIZE = 32;
    public static final int TWITTER_FIELD_NAME = 0;
    public static final int TWITTER_FIELD_VALUE = 1;
    public static final int TWITTER_MAX_CONSUME_TWEETS_BY_REQUEST = 5;
    public static final int TWITTER_PER_USER_READING_TWEETS_LIMIT_PER_HOUR = 5;
    public static final int MASTODON_PER_USER_READING_POSTS_LIMIT_PER_HOUR = 500;
    public static final int MASTODON_POSTS_PAGE_SIZE = 20;
    public static final String PINGEN_TWITTER_ACCESS_TOKEN_HEADER = "x-access-token";
    public static final String PINGEN_TWITTER_ACCESS_TOKEN_SECRET_HEADER = "x-access-secret";
    public static final String PINGEN_TWITTER_SINCE_ID_EXCLUDED_PARAM = "sinceId";
    public static final String PINGEN_TWITTER_UNTIL_ID_EXCLUDED_PARAM = "untilId";
    public static final String PINGEN_TWITTER_PAGE_TOKEN_PARAM = "twitterPaginationToken";
    public static final String MASTODON_OAUTH_TOKEN = "/oauth/token";
    public static final String MASTODON_VERIFY_CREDENTIALS = "/api/v1/apps/verify_credentials";
    public static final long HTTP_CLIENT_TIMEOUT = 30000;
    public static final MediaType HTTP_CLIENT_MEDIA_TYPE_JSON = Objects.requireNonNull(MediaType.parse("application/json; " +
            "charset=utf-8"));
}