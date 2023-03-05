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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Collection;
import java.util.function.BiConsumer;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<className>.+)$",
        replaceRule = "{basePackage}.{platform}.{Platform}{className}"
)
public abstract class CustomPayload {
    private static @Nullable CustomPayload customPayload;

    @ApiStatus.Internal
    public CustomPayload() {
        Preconditions.checkArgument(customPayload == null, "CustomPayload has been already initialized!");
        customPayload = this;
    }

    public static void registerOutgoingChannel(@NotNull String channel) {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").registerOutgoingChannel0(channel);
    }

    public static void unregisterOutgoingChannel(@NotNull String channel) {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").unregisterOutgoingChannel0(channel);
    }

    public static void unregisterAllOutgoingChannels() {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").unregisterAllOutgoingChannels0();
    }

    public static @NotNull Collection<@NotNull String> getRegisteredOutgoingChannels() {
        return Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").getRegisteredOutgoingChannels0();
    }

    public static @NotNull Registration registerIncomingChannel(@NotNull String channel, @NotNull BiConsumer<@NotNull Player, byte @NotNull []> listener) {
        return Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").registerIncomingChannel0(channel, listener);
    }

    public static void unregisterIncomingChannel(@NotNull Registration registration) {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").unregisterIncomingChannel0(registration);
    }

    public static void unregisterAllIncomingChannels() {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").unregisterAllIncomingChannels0();
    }

    public static @NotNull Collection<@NotNull String> getRegisteredIncomingChannels() {
        return Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").getRegisteredIncomingChannels0();
    }

    public static void send(@NotNull Player player, @NotNull String channel, byte @NotNull [] payload) {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").send0(player, channel, payload);
    }

    public static void send(@NotNull String channel, byte @NotNull [] payload) {
        Preconditions.checkNotNull(customPayload, "CustomPayload is not initialized yet!").send0(channel, payload);
    }

    protected abstract void registerOutgoingChannel0(@NotNull String channel);

    protected abstract void unregisterOutgoingChannel0(@NotNull String channel);

    protected abstract void unregisterAllOutgoingChannels0();

    protected abstract @NotNull Collection<@NotNull String> getRegisteredOutgoingChannels0();

    protected abstract @NotNull Registration registerIncomingChannel0(@NotNull String channel, @NotNull BiConsumer<@NotNull Player, byte @NotNull[]> listener);

    protected abstract void unregisterIncomingChannel0(@NotNull Registration registration);

    protected abstract void unregisterAllIncomingChannels0();

    protected abstract @NotNull Collection<@NotNull String> getRegisteredIncomingChannels0();

    protected abstract void send0(@NotNull Player player, @NotNull String channel, byte @NotNull [] payload);

    protected abstract void send0(@NotNull String channel, byte @NotNull [] payload);

    public interface Registration extends Wrapper {

    }
}
