package com.anyqn.mastodon.mastosync.fabrics;


import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;

import static com.anyqn.mastodon.common.Constants.DEFAULT_JDBC_URL;

public enum LocalDbConnectionFabric implements ISingletonFabric<Connection> {
    IT;

    private final Connection connection;

    @SneakyThrows
    LocalDbConnectionFabric() {
        SQLiteDataSource sqliteDataSource = new SQLiteDataSource();
        sqliteDataSource.setUrl(DEFAULT_JDBC_URL);
        connection = sqliteDataSource.getConnection();
        Flyway flyway = Flyway.configure()
                .dataSource(sqliteDataSource)
                .load();
        flyway.migrate();
    }

    @Override
    @SneakyThrows
    public Connection getInstance() {
        return connection;
    }
}
