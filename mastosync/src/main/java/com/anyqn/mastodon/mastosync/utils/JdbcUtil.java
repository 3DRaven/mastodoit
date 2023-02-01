package com.anyqn.mastodon.mastosync.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public abstract class JdbcUtil {
    public static Optional<String> getOptionalStringValue(PreparedStatement preparedStatement, String columnName) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.ofNullable(resultSet.getString(columnName));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Long> getOptionalNonZeroLongValue(PreparedStatement preparedStatement, String columnName) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            long value = resultSet.getLong(columnName);
            if (value == 0) {
                return Optional.empty();
            } else {
                return Optional.of(value);
            }
        } else {
            return Optional.empty();
        }
    }

    public static boolean getMandatoryBooleanValue(PreparedStatement preparedStatement, String columnName) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getBoolean(columnName);
        } else {
            throw new IllegalStateException(String.format("Record not found for column [%s]", columnName));
        }
    }
}
