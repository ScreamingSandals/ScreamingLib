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
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListenerRegistration;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.CustomPayload;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Collection;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class BukkitCustomPayload extends CustomPayload {
    private final Plugin plugin;

    @Override
    protected void registerOutgoingChannel0(String channel) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, channel);
    }

    @Override
    protected void unregisterOutgoingChannel0(String channel) {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, channel);
    }

    @Override
    protected void unregisterAllOutgoingChannels0() {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin);
    }

    @Override
    protected Collection<String> getRegisteredOutgoingChannels0() {
        return Bukkit.getMessenger().getOutgoingChannels(plugin);
    }

    @Override
    protected Registration registerIncomingChannel0(String channel, BiConsumer<PlayerWrapper, byte[]> listener) {
        return new BukkitRegistration(Bukkit.getMessenger().registerIncomingPluginChannel(plugin, channel, (channel1, player, message) -> {
            if (channel1.equals(channel)) {
                listener.accept(new BukkitEntityPlayer(player), message);
            }
        }));
    }

    @Override
    protected void unregisterIncomingChannel0(Registration registration) {
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
    protected Collection<String> getRegisteredIncomingChannels0() {
        return Bukkit.getMessenger().getIncomingChannels(plugin);
    }

    @Override
    protected void send0(PlayerWrapper player, String channel, byte[] payload) {
        player.as(Player.class).sendPluginMessage(plugin, channel, payload);
    }

    @Override
    protected void send0(String channel, byte[] payload) {
        Bukkit.getServer().sendPluginMessage(plugin, channel, payload);
    }

    public static class BukkitRegistration extends BasicWrapper<PluginMessageListenerRegistration> implements Registration {

        protected BukkitRegistration(@NotNull PluginMessageListenerRegistration wrappedObject) {
            super(wrappedObject);
        }
    }
}
