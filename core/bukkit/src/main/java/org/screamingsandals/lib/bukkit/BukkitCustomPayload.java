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

package org.screamingsandals.lib.bukkit;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListenerRegistration;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.CustomPayload;
import org.screamingsandals.lib.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Collection;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class BukkitCustomPayload extends CustomPayload {
    private final @NotNull Plugin plugin;

    @Override
    protected void registerOutgoingChannel0(@NotNull String channel) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, channel);
    }

    @Override
    protected void unregisterOutgoingChannel0(@NotNull String channel) {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, channel);
    }

    @Override
    protected void unregisterAllOutgoingChannels0() {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin);
    }

    @Override
    protected @NotNull Collection<@NotNull String> getRegisteredOutgoingChannels0() {
        return Bukkit.getMessenger().getOutgoingChannels(plugin);
    }

    @Override
    protected @NotNull Registration registerIncomingChannel0(@NotNull String channel, @NotNull BiConsumer<@NotNull Player, byte @NotNull []> listener) {
        return new BukkitRegistration(Bukkit.getMessenger().registerIncomingPluginChannel(plugin, channel, (channel1, player, message) -> {
            if (channel1.equals(channel)) {
                listener.accept(new BukkitPlayer(player), message);
            }
        }));
    }

    @Override
    protected void unregisterIncomingChannel0(@NotNull Registration registration) {
        if (!(registration instanceof BukkitRegistration)) {
            throw new UnsupportedOperationException("Registration is not instance of BukkitRegistration class!");
        }
        var bRegistration = registration.as(PluginMessageListenerRegistration.class);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(bRegistration.getPlugin(), bRegistration.getChannel(), bRegistration.getListener());
    }

    @Override
    protected void unregisterAllIncomingChannels0() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin);
    }

    @Override
    protected @NotNull Collection<@NotNull String> getRegisteredIncomingChannels0() {
        return Bukkit.getMessenger().getIncomingChannels(plugin);
    }

    @Override
    protected void send0(Player player, @NotNull String channel, byte @NotNull [] payload) {
        player.as(org.bukkit.entity.Player.class).sendPluginMessage(plugin, channel, payload);
    }

    @Override
    protected void send0(@NotNull String channel, byte @NotNull [] payload) {
        Bukkit.getServer().sendPluginMessage(plugin, channel, payload);
    }

    public static class BukkitRegistration extends BasicWrapper<PluginMessageListenerRegistration> implements Registration {

        protected BukkitRegistration(@NotNull PluginMessageListenerRegistration wrappedObject) {
            super(wrappedObject);
        }
    }
}
