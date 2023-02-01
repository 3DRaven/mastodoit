package com.anyqn.mastodon.mastosync.repostitories.local;

import com.anyqn.mastodon.common.models.values.mastodon.MastodonNotificationId;
import com.anyqn.mastodon.common.models.values.mastodon.MastodonStatusId;
import com.anyqn.mastodon.common.models.values.twitter.TweetId;
import com.anyqn.mastodon.mastosync.fabrics.StatementsFabric;
import com.anyqn.mastodon.mastosync.utils.JdbcUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.util.Optional;

@Slf4j
public class LocalPostsRepository {
    private final StatementsFabric statementsFabric = StatementsFabric.IT;
    private final static String getPostSql = "SELECT * FROM tweets WHERE mastodon_status_id = ?;";
    private final static String getCountPostByStatusIdSql = "SELECT COUNT(*) > 0 as existence FROM tweets WHERE " +
            "mastodon_status_id = ?;";
    private final static String getCountPostByNotificationIdSql = "SELECT COUNT(*) > 0 as existence FROM tweets WHERE" +
            " mastodon_notification_id = ?;";
    private final static String getLastFetchedMastodonStatusIdSql = "SELECT MAX(CAST(mastodon_status_id as INTEGER)) " +
            "as last_id FROM tweets;";
    private final static String getLastFetchedMastodonNotificationIdSql = "SELECT MAX(CAST(mastodon_notification_id " +
            "as INTEGER)) as last_id FROM tweets;";

    @SneakyThrows
    public Optional<TweetId> getAssociatedTweetId(MastodonStatusId mastodonStatusId) {
        PreparedStatement statement = statementsFabric.getPreparedStatement(getPostSql);
        statement.clearParameters();
        statement.setString(1, mastodonStatusId.getValue());
        return JdbcUtil.getOptionalStringValue(statement, "tweet_id").map(TweetId::new);
    }

    @SneakyThrows
    public boolean isPostPublished(MastodonStatusId mastodonStatusId) {
        PreparedStatement statement = statementsFabric.getPreparedStatement(getCountPostByStatusIdSql);
        statement.clearParameters();
        statement.setString(1, mastodonStatusId.getValue());
        return JdbcUtil.getMandatoryBooleanValue(statement, "existence");
    }

    @SneakyThrows
    public boolean isNotificationPublished(MastodonNotificationId mastodonNotificationId) {
        PreparedStatement statement = statementsFabric.getPreparedStatement(getCountPostByNotificationIdSql);
        statement.clearParameters();
        statement.setString(1, mastodonNotificationId.getValue());
        return JdbcUtil.getMandatoryBooleanValue(statement, "existence");
    }

    @SneakyThrows
    public Optional<MastodonStatusId> getLastFetchedMastodonStatusId() {
        PreparedStatement preparedStatement = statementsFabric.getPreparedStatement(getLastFetchedMastodonStatusIdSql);
        preparedStatement.clearParameters();
        return JdbcUtil.getOptionalNonZeroLongValue(preparedStatement, "last_id")
                .map(MastodonStatusId::new);
    }

    @SneakyThrows
    public Optional<MastodonNotificationId> getLastFetchedMastodonNotificationId() {
        PreparedStatement preparedStatement = statementsFabric.getPreparedStatement(getLastFetchedMastodonNotificationIdSql);
        preparedStatement.clearParameters();
        return JdbcUtil.getOptionalNonZeroLongValue(preparedStatement, "last_id")
                .map(MastodonNotificationId::new);
    }

}
