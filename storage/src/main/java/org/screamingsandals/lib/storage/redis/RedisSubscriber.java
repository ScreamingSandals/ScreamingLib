package org.screamingsandals.lib.storage.redis;

import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import lombok.*;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.tasker.BaseTask;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class RedisSubscriber extends RedisPubSubAdapter<String, String> {
    @Getter
    private String channel;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private transient StatefulRedisPubSubConnection<String, String> connection;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private transient RedisPubSubAsyncCommands<String, String> commands;

    public RedisSubscriber(String channel) {
        this.channel = channel;
    }

    public void destroy() {
        Debug.info("RedisSubscriber> Unsubscribing from channel " + channel);
        connection.removeListener(this);
        commands.unsubscribe(channel);
    }

    public void initialize(StatefulRedisPubSubConnection<String, String> connection) {
        this.connection = connection;
        this.commands = connection.async();

        subscribe();
    }

    private void subscribe() {
        RedisStorage.getTasker().runTaskAsync(new BaseTask() {
            @Override
            public void run() {
                try {
                    Debug.info("RedisSubscriber> trying to subscribe channel " + channel);
                    connection.addListener(RedisSubscriber.this);
                    commands.subscribe(channel);
                    Debug.info("RedisSubscriber> done!");
                } catch (Exception e) {
                    Debug.info("RedisSender> Error while subscribing to channel " + channel, true);
                    e.printStackTrace();

                }
            }
        });
    }

    @Override
    public void message(String channel, String message) {

    }
}