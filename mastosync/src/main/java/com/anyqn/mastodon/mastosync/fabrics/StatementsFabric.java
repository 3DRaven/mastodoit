package com.anyqn.mastodon.mastosync.fabrics;

import lombok.SneakyThrows;

import java.sql.PreparedStatement;

public enum StatementsFabric {
    IT;

    private final LocalDbConnectionFabric localDbConnectionFabric = LocalDbConnectionFabric.IT;

    @SneakyThrows
    public synchronized PreparedStatement getPreparedStatement(String sql) {
        //It is local connection to sqlite db, just leave it KISS, it is not expensive for this app
        return localDbConnectionFabric.getInstance().prepareStatement(sql);
    }
}
