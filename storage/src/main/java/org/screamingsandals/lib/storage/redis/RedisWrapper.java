package org.screamingsandals.lib.storage.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.Data;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.tasker.BaseTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public class RedisWrapper {
    private final String redisAddress;
    private final int redisPort;
    private final String clientName;
    private final HashMap<String, RedisSubscriber> redisSubscribersMap = new HashMap<>();

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> redisConnection;
    private StatefulRedisPubSubConnection<String, String> subscribersConnection;
    private StatefulRedisPubSubConnection<String, String> sendersConnection;

    private RedisCommands<String, String> commands;
    private RedisAsyncCommands<String, String> asyncCommands;

    public RedisWrapper(String redisAddress, int redisPort, String clientName) {
        this.redisAddress = redisAddress;
        this.redisPort = redisPort;
        this.clientName = clientName;

        load();
    }

    //TODO: do this more configurable
    private void load() {
        RedisURI redisURI = new RedisURI(redisAddress, redisPort, Duration.ofSeconds(10));
        redisURI.setClientName(clientName);

        ClientResources clientResources = DefaultClientResources.create();
        redisClient = RedisClient.create(clientResources, redisURI);
        redisClient.setOptions(ClientOptions.builder().autoReconnect(true).build());
        redisConnection = redisClient.connect();

        Debug.info("RedisWrapper> Connected to Redis on " + redisAddress + ":" + redisPort);
        Debug.info("RedisWrapper> Redis client name on RedisURI is: " + redisURI.getClientName());

        subscribersConnection = redisClient.connectPubSub();
        sendersConnection = redisClient.connectPubSub();

        Debug.info("RedisWrapper> Connection ping " + redisConnection.sync().ping());

        commands = redisConnection.sync();
        asyncCommands = redisConnection.async();

        Debug.info("RedisWrapper> Commands ping " + commands.ping());
    }

    public void destroy() {
        redisSubscribersMap.values().forEach(subscriber -> {
            if (subscriber != null) {
                subscriber.destroy();
            }
        });

        redisSubscribersMap.clear();
        redisClient.shutdown(0, 1, TimeUnit.SECONDS);
    }

    /**
     * Subscribes to Redis channel
     *
     * @param redisSubscriber subscriber instance
     */
    public void subscribe(RedisSubscriber redisSubscriber) {
        String channel = redisSubscriber.getChannel();
        if (!redisSubscribersMap.containsKey(toLowerCase(channel))) {
            redisSubscribersMap.put(toLowerCase(channel), redisSubscriber);
            redisSubscriber.initialize(subscribersConnection);
        }
    }

    /**
     * Unsubscribes from the channel
     *
     * @param channel redis key
     */
    public void unsubscribe(String channel) {
        RedisSubscriber redisSubscriber = redisSubscribersMap.get(channel);
        if (redisSubscriber != null) {
            unsubscribe(redisSubscriber);
        }
    }

    /**
     * Unsubscribes from the channel
     *
     * @param redisSubscriber subscriber instance
     */
    public void unsubscribe(RedisSubscriber redisSubscriber) {
        redisSubscriber.destroy();
        redisSubscribersMap.remove(redisSubscriber.getChannel());
    }

    /**
     * Publishes data to the Redis
     *
     * @param channel redis channel
     * @param message message
     */
    public void publish(String channel, String message) {
        RedisSender redisSender = new RedisSender(sendersConnection, channel);
        redisSender.send(message);
    }

    /**
     * Saves the message to the Redis key
     *
     * @param key     redis key
     * @param message string to be saved
     */
    public void set(String key, String message) {
        Debug.info("RedisFactory-set()> saving to key " + key);
        Debug.info("RedisFactory-set()> with message " + message);
        RedisStorage.getTasker().runTaskAsync(new BaseTask() {
            @Override
            public void run() {
                getAsyncCommands().set(toLowerCase(key), message);
            }
        });
    }

    /**
     * Gets the message from the redis key
     *
     * @param key redis key
     * @return string from redis key
     */
    public String get(String key) {
        Debug.info("RedisFactory-get()> getting key " + key);
        String message = getCommands().get(toLowerCase(key));

        Debug.info("RedisFactory-get()> Got it! Message is: " + message);
        return message;
    }

    /**
     * Checks if the key has any data
     *
     * @param key redis key
     * @return true if there is ANY data in the key or if the key exists
     */
    public boolean exists(String key) {
        Debug.info("RedisFactory-exists()> asking if key exists: " + key);
        boolean exists = getCommands().exists(toLowerCase(key)) >= 1;

        Debug.info("RedisFactory-exists()> Got it! Result is: " + exists);
        return exists;
    }

    /**
     * Gets all keys
     *
     * @param key redis key
     * @return List of keys
     */
    public List<String> getKeys(String key) {
        return getCommands().keys(key);
    }

    private String toLowerCase(String channel) {
        return channel.toLowerCase();
    }

}
