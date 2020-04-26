package org.screamingsandals.lib.storage.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.storage.Storage;
import org.screamingsandals.lib.tasker.Tasker;

@EqualsAndHashCode(callSuper = false)
@Data
public class RedisStorage extends Storage {
    private static RedisStorage redisStorage;
    private final Tasker tasker;

    public RedisStorage(Tasker tasker) {
        this.tasker = tasker;
        redisStorage = this;
    }

    public static Tasker getTasker() {
        return redisStorage.tasker;
    }
}
