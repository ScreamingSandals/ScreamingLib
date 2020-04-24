package org.screamingsandals.gamecore.core.data.redis;

import org.screamingsandals.gamecore.core.data.DataSource;

import java.io.File;

public class RedisDataSource<T> extends DataSource<T> {

    public RedisDataSource(File file) {
        super(file);
    }
}
