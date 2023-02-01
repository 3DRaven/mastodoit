CREATE TABLE IF NOT EXISTS twitter_auth_headers
(
    header_id INTEGER    NOT NULL PRIMARY KEY AUTOINCREMENT,
    "method"  TEXT(10)   NOT NULL,
    url       TEXT(4096) NOT NULL,
    token     TEXT(512)  NOT NULL,
    params    TEXT(4096),
    header    TEXT(4096) NOT NULL
);
CREATE INDEX IF NOT EXISTS twitter_auth_headers_method_IDX ON twitter_auth_headers ("method");
CREATE INDEX IF NOT EXISTS twitter_auth_headers_token_IDX ON twitter_auth_headers ("token");
CREATE INDEX IF NOT EXISTS twitter_auth_headers_url_IDX ON twitter_auth_headers (url);
CREATE INDEX IF NOT EXISTS twitter_auth_headers_params_IDX ON twitter_auth_headers (params);

CREATE TABLE IF NOT EXISTS tweets
(
    tweet_id                 TEXT      NOT NULL PRIMARY KEY,
    parent_id                TEXT,               -- tweet id replayed by this tweet
    reference_type           INTEGER,
    possibly_sensitive       INTEGER,
    author_id                TEXT      NOT NULL,
    conversation_id          TEXT,               -- root tweet id for tree of conversation for this tweet
    created_at               INTEGER   NOT NULL,
    reply_settings           INTEGER   NOT NULL,
    mastodon_status_id       TEXT,               -- mastodon post id associated with this tweet
    mastodon_notification_id TEXT,               -- mastodon notification associated with this post id
    text                     TEXT(250) NOT NULL,
    is_deleted               INTEGER   NOT NULL, -- true - deleted from tweeter
    source                   TEXT      NOT NULL
);
CREATE INDEX IF NOT EXISTS tweets_parent_id_IDX ON tweets (parent_id);
CREATE INDEX IF NOT EXISTS tweets_author_id_IDX ON tweets (author_id);
CREATE INDEX IF NOT EXISTS tweets_conversation_id_IDX ON tweets (conversation_id);
CREATE INDEX IF NOT EXISTS tweets_mastodon_id_IDX ON tweets (mastodon_status_id);
CREATE INDEX IF NOT EXISTS tweets_is_deleted_IDX ON tweets (is_deleted);
CREATE INDEX IF NOT EXISTS tweets_source_IDX ON tweets (source);