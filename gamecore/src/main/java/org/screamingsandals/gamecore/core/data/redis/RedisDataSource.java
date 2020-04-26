package org.screamingsandals.gamecore.core.data.redis;

import org.screamingsandals.gamecore.core.data.DataSource;

public class RedisDataSource<T> extends DataSource<T> {

    public RedisDataSource(Class<T> tClass) {
        super(tClass);
    }
}
