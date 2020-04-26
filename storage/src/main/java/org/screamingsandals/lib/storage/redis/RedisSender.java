package org.screamingsandals.lib.storage.redis;

import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.tasker.BaseTask;

public class RedisSender {
    private final RedisPubSubAsyncCommands<String, String> commands;
    private final String channel;

    public RedisSender(StatefulRedisPubSubConnection<String, String> connection, String channel) {
        this.commands = connection.async();
        this.channel = channel;
    }

    public void send(String message) {
        RedisStorage.getTasker().runTaskAsync(new BaseTask() {
            @Override
            public void run() {
                try {
                    Debug.info("RedisSender> sending to channel " + channel);
                    Debug.info("RedisSender> with message " + message);
                    commands.publish(channel, message);

                } catch (Exception e) {
                    Debug.info("RedisSender> Error while sending message on channel " + channel, true);
                    e.printStackTrace();
                }

            }
        });
    }

}