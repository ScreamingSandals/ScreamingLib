package org.screamingsandals.lib;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Collection;
import java.util.function.BiConsumer;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<className>.+)$",
        replaceRule = "{basePackage}.{platform}.{Platform}{className}"
)
public abstract class CustomPayload {
    private static CustomPayload customPayload;

    @ApiStatus.Internal
    public CustomPayload() {
        if (customPayload != null) {
            throw new UnsupportedOperationException("CustomPayload has been already initialized!");
        }
        customPayload = this;
    }

    public static void registerOutgoingChannel(String channel) {
        if (customPayload == null) {
            throw new UnsupportedOperationException("CustomPayload is not initialized yet!");
        }
        customPayload.registerOutgoingChannel0(channel);
    }

    public static void unregisterOutgoingChannel(String channel) {
        if (customPayload == null) {
            throw new UnsupportedOperationException("CustomPayload is not initialized yet!");
        }
        customPayload.unregisterOutgoingChannel0(channel);
    }

    public static void unregisterAllOutgoingChannels() {
        if (customPayload == null) {
            throw new UnsupportedOperationException("CustomPayload is not initialized yet!");
        }
        customPayload.unregisterAllOutgoingChannels0();
    }

    public static Collection<String> getRegisteredOutgoingChannels() {
        if (customPayload == null) {
            throw new UnsupportedOperationException("CustomPayload is not initialized yet!");
        }
        return customPayload.getRegisteredOutgoingChannels0();
    }

    public static Registration registerIncomingChannel(String channel, BiConsumer<PlayerWrapper, byte[]> listener) {
        if (customPayload == null) {
            throw new UnsupportedOperationException("CustomPayload is not initialized yet!");
        }
        return customPayload.registerIncomingChannel0(channel, listener);
    }

    public static void unregisterIncomingChannel(Registration registration) {
        if (customPayload == null) {
            throw new UnsupportedOperationException("CustomPayload is not initialized yet!");
        }
        customPayload.unregisterIncomingChannel0(registration);
    }

    public static void unregisterAllIncomingChannels() {
        if (customPayload == null) {
            throw new UnsupportedOperationException("CustomPayload is not initialized yet!");
        }
        customPayload.unregisterAllIncomingChannels0();
    }

    public static Collection<String> getRegisteredIncomingChannels() {
        if (customPayload == null) {
            throw new UnsupportedOperationException("CustomPayload is not initialized yet!");
        }
        return customPayload.getRegisteredIncomingChannels0();
    }

    public static void send(PlayerWrapper player, String channel, byte[] payload) {
        if (customPayload == null) {
            throw new UnsupportedOperationException("CustomPayload is not initialized yet!");
        }
        customPayload.send0(player, channel, payload);
    }

    public static void send(String channel, byte[] payload) {
        if (customPayload == null) {
            throw new UnsupportedOperationException("CustomPayload is not initialized yet!");
        }
        customPayload.send0(channel, payload);
    }

    protected abstract void registerOutgoingChannel0(String channel);

    protected abstract void unregisterOutgoingChannel0(String channel);

    protected abstract void unregisterAllOutgoingChannels0();

    protected abstract Collection<String> getRegisteredOutgoingChannels0();

    protected abstract Registration registerIncomingChannel0(String channel, BiConsumer<PlayerWrapper, byte[]> listener);

    protected abstract void unregisterIncomingChannel0(Registration registration);

    protected abstract void unregisterAllIncomingChannels0();

    protected abstract Collection<String> getRegisteredIncomingChannels0();

    protected abstract void send0(PlayerWrapper player, String channel, byte[] payload);

    protected abstract void send0(String channel, byte[] payload);

    public interface Registration extends Wrapper {

    }
}
