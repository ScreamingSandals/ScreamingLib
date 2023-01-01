/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.Preconditions;
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
        Preconditions.checkArgument(customPayload == null, "CustomPayload has been already initialized!");
        customPayload = this;
    }

    public static void registerOutgoingChannel(String channel) {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").registerOutgoingChannel0(channel);
    }

    public static void unregisterOutgoingChannel(String channel) {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").unregisterOutgoingChannel0(channel);
    }

    public static void unregisterAllOutgoingChannels() {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").unregisterAllOutgoingChannels0();
    }

    public static Collection<String> getRegisteredOutgoingChannels() {
        return Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").getRegisteredOutgoingChannels0();
    }

    public static Registration registerIncomingChannel(String channel, BiConsumer<PlayerWrapper, byte[]> listener) {
        return Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").registerIncomingChannel0(channel, listener);
    }

    public static void unregisterIncomingChannel(Registration registration) {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").unregisterIncomingChannel0(registration);
    }

    public static void unregisterAllIncomingChannels() {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").unregisterAllIncomingChannels0();
    }

    public static Collection<String> getRegisteredIncomingChannels() {
        return Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").getRegisteredIncomingChannels0();
    }

    public static void send(PlayerWrapper player, String channel, byte[] payload) {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").send0(player, channel, payload);
    }

    public static void send(String channel, byte[] payload) {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").send0(channel, payload);
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
