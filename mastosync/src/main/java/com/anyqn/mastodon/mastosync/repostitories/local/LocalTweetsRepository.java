package com.anyqn.mastodon.mastosync.repostitories.local;

import com.anyqn.mastodon.common.models.twitter.tweets.PublishedTweet;
import com.anyqn.mastodon.common.models.values.TypedValue;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonNotificationId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.common.util.Fluent;
import com.anyqn.mastodon.mastosync.enums.MessageSource;
import com.anyqn.mastodon.mastosync.fabrics.StatementsFabric;
import com.anyqn.mastodon.mastosync.utils.JdbcUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.PreparedStatement;
import java.util.Optional;

@Slf4j
public class LocalTweetsRepository {
    private final StatementsFabric statementsFabric = StatementsFabric.IT;
    private final static String saveTweetSql = "INSERT INTO tweets (tweet_id, parent_id, reference_type, " +
            "possibly_sensitive, " +
            "author_id, conversation_id, created_at, reply_settings, mastodon_status_id, mastodon_notification_id, " +
            "\"text\", is_deleted, " +
            "source) " +
            "VALUES(?, ?, " +
            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final static String markDeletedSql = "UPDATE tweets SET is_deleted=true WHERE tweet_id = ?";
    private final static String getLastFetchedTweetIdSql = "SELECT MAX(CAST(tweet_id as INTEGER)) as max FROM tweets " +
            "WHERE source = 'TWITTER_REPLY'";
    private final static String getFirstCreatedFromPostTweetIdSql = "SELECT MIN(CAST(tweet_id as INTEGER)) as min FROM tweets WHERE " +
            "source != 'TWITTER_REPLY'";
    private final static String getNextCreatedFromPostTweetIdSinceSql = "SELECT MIN(CAST(tweet_id as INTEGER)) as next FROM tweets WHERE " +
            "source != 'TWITTER_REPLY' AND CAST(tweet_id as INTEGER) > CAST(? as INTEGER);";
    private final static String getConversationIdSql = "SELECT conversation_id FROM tweets WHERE tweet_id = ?;";
    private final static String getTweetSql = "SELECT * FROM tweets WHERE tweet_id = ?;";

    @SneakyThrows
    public Optional<TweetId> getLastFetchedTweetId() {
        return JdbcUtil.getOptionalStringValue(statementsFabric.getPreparedStatement(getLastFetchedTweetIdSql), "max")
                .map(TweetId::new);
    }

    @SneakyThrows
    public Optional<TweetId> getNextCreatedFromPostTweetIdSince(TweetId sinceId) {
        PreparedStatement preparedStatement = statementsFabric.getPreparedStatement(getNextCreatedFromPostTweetIdSinceSql);
        preparedStatement.clearParameters();
        preparedStatement.setString(1, sinceId.getValue());
        return JdbcUtil.getOptionalStringValue(preparedStatement, "next").map(TweetId::new);
    }

    @SneakyThrows
    public Optional<TweetId> getFirstCreatedTweetId() {
        PreparedStatement preparedStatement = statementsFabric.getPreparedStatement(getFirstCreatedFromPostTweetIdSql);
        return JdbcUtil.getOptionalStringValue(preparedStatement, "min").map(TweetId::new);
    }

    @SneakyThrows
    public Optional<TweetId> getConversationId(TweetId tweetId) {
        PreparedStatement preparedStatement = statementsFabric.getPreparedStatement(getConversationIdSql);
        preparedStatement.clearParameters();
        preparedStatement.setString(1, tweetId.getValue());
        return JdbcUtil.getOptionalStringValue(preparedStatement, "conversation_id").map(TweetId::new);
    }

    @SneakyThrows
    public boolean isTweetExists(TweetId tweetId) {
        PreparedStatement statement = statementsFabric.getPreparedStatement(getTweetSql);
        statement.clearParameters();
        statement.setString(1, tweetId.getValue());
        return JdbcUtil.getOptionalStringValue(statement, "tweet_id").isPresent();
    }

    @SneakyThrows
    public boolean isTweetDeleted(TweetId tweetId) {
        PreparedStatement statement = statementsFabric.getPreparedStatement(getTweetSql);
        statement.clearParameters();
        statement.setString(1, tweetId.getValue());
        return JdbcUtil.getMandatoryBooleanValue(statement, "is_deleted");
    }

    @SneakyThrows
    public Optional<MastodonStatusId> getAssociatedMastodonId(TweetId tweetId) {
        PreparedStatement statement = statementsFabric.getPreparedStatement(getTweetSql);
        statement.clearParameters();
        statement.setString(1, tweetId.getValue());
        return JdbcUtil.getOptionalStringValue(statement, "mastodon_id").map(MastodonStatusId::new);
    }

    /**
     * @param publishedTweet         tweet data published in tweeter (we can create tweet in tweeter or fetch tweets created
     *                               by other users)
     * @param mastodonStatusId       associated mastodon post for this tweet
     * @param mastodonNotificationId associated source mastodon notification for this status
     * @param messageSource          source of this tweet, from mastodon post creation, or from Twitter reply
     */
    @SneakyThrows
    public void savePublishedTweet(PublishedTweet publishedTweet,
                                   MastodonStatusId mastodonStatusId,
                                   @Nullable MastodonNotificationId mastodonNotificationId,
                                   MessageSource messageSource) {
        PreparedStatement preparedStatement = statementsFabric.getPreparedStatement(saveTweetSql);
        preparedStatement.clearParameters();
        //tweet_id
        preparedStatement.setString(1, publishedTweet.getId().toString());
        //parent_id
        publishedTweet.getParentId().ifPresent(tweetId ->
                Fluent.runAndRethrow(() -> preparedStatement.setString(2, tweetId.getValue())));
        //reference_type
        publishedTweet.getReplyType().ifPresent(referenceType ->
                Fluent.runAndRethrow(() -> preparedStatement.setInt(3, referenceType.getAdaptedValue())));
        //possibly_sensitive
        preparedStatement.setInt(4, BooleanUtils.toInteger(publishedTweet.isPossiblySensitive()));
        //author_id
        preparedStatement.setString(5, publishedTweet.getAuthorId().toString());
        //conversation_id
        preparedStatement.setString(6,
                Optional.ofNullable(publishedTweet.getConversationId()).map(TypedValue::getValue).orElse(null));
        //created_at
        preparedStatement.setLong(7, publishedTweet.getCreatedAt().toEpochSecond());
        //reply_settings
        preparedStatement.setInt(8, publishedTweet.getReplySettings().getAdaptedValue());
        //mastodon_status_id
        preparedStatement.setString(9, mastodonStatusId.getValue());
        //mastodon_notification_id
        preparedStatement.setString(10,
                Optional.ofNullable(mastodonNotificationId).map(TypedValue::getValue).orElse(null));
        //text
        preparedStatement.setString(11, publishedTweet.getText());
        //is_deleted
        preparedStatement.setInt(12, publishedTweet.isDeleted() ? 1 : 0);
        //source
        preparedStatement.setString(13, messageSource.name());
        preparedStatement.execute();
    }

    @SneakyThrows
    public void markDeleted(TweetId tweetId) {
        PreparedStatement preparedStatement = statementsFabric.getPreparedStatement(markDeletedSql);
        preparedStatement.clearParameters();
        //tweet_id
        preparedStatement.setString(1, tweetId.getValue());
        preparedStatement.execute();
    }


}
