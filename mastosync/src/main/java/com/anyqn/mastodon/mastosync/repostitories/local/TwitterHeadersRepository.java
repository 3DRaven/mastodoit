package com.anyqn.mastodon.mastosync.repostitories.local;

import com.anyqn.mastodon.common.models.values.twitter.TwitterUserAccessToken;
import com.anyqn.mastodon.mastosync.fabrics.StatementsFabric;
import lombok.SneakyThrows;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpMethod;

import java.util.Optional;

public class TwitterHeadersRepository {
    private final StatementsFabric statementsFabric = StatementsFabric.IT;
    private final static String saveTwitterAuthHeaderSql = "INSERT INTO twitter_auth_headers (\"method\", url, " +
            "params, " +
            "header,token) VALUES(?, ?, ?, ?, ?);";

    private final static String getTwitterAuthHeaderSql = "SELECT header_id, \"method\", url, params, header FROM twitter_auth_headers " +
            "WHERE \"method\"=? and url=? and params=? and token=?;";

    @SneakyThrows
    public void saveTwitterAuthHeader(TwitterUserAccessToken twitterUserAccessToken,
                                      HttpMethod method,
                                      String url,
                                      @Nullable String params,
                                      String header) {
        //We do not close statements at all, just reuse them
        var savingStatement = statementsFabric.getPreparedStatement(saveTwitterAuthHeaderSql);
        savingStatement.setString(1, method.name());
        savingStatement.setString(2, url);
        savingStatement.setString(3, params);
        savingStatement.setString(4, header);
        savingStatement.setString(5, twitterUserAccessToken.getValue());
        savingStatement.execute();
    }

    @SneakyThrows
    public Optional<String> getTwitterAuthHeader(TwitterUserAccessToken twitterUserAccessToken, HttpMethod method,
                                                 String url,
                                                 @Nullable String params) {
        //We do not close statements at all, just reuse them
        var savingStatement = statementsFabric.getPreparedStatement(getTwitterAuthHeaderSql);
        savingStatement.setString(1, method.name());
        savingStatement.setString(2, url);
        savingStatement.setString(3, params);
        savingStatement.setString(4,twitterUserAccessToken.getValue());
        var resultSet = savingStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.ofNullable(resultSet.getString("header"));
        } else {
            return Optional.empty();
        }
    }
}
