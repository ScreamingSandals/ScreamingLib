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

package org.screamingsandals.lib.impl.bukkit;

import io.netty.channel.ChannelFuture;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.player.GenericCommandSender;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.bukkit.utils.Version;
import org.screamingsandals.lib.nms.accessors.*;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.player.Sender;
import org.screamingsandals.lib.utils.ProxyType;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BukkitServer extends Server {
    private static final @NotNull Map<@NotNull String, String> UNSAFE_SOUND_CACHE = new HashMap<>();

    {
        try {
            UNSAFE_SOUND_CACHE.clear();

            if (Reflect.hasMethod(Sound.class, "getKey")) {
                for (var v : Sound.values()) {
                    if ("minecraft".equals(v.getKey().getNamespace())) {
                        UNSAFE_SOUND_CACHE.put(v.name(), v.getKey().getKey());
                    }
                }
            } else {
                for (var v : Sound.values()) {
                    var craftSound = Reflect.getMethod(ClassStorage.CB.CraftSound, "getSound", Sound.class).invokeStatic(v);
                    UNSAFE_SOUND_CACHE.put(v.name(), craftSound.toString());
                }
            }
        } catch (Throwable ignored) {
        }
    }

    @Override
    public @NotNull String getVersion0() {
        if (Version.PATCH_VERSION == 0) {
            return Version.MAJOR_VERSION + "." + Version.MINOR_VERSION;
        }
        return Version.MAJOR_VERSION + "." + Version.MINOR_VERSION + "." + Version.PATCH_VERSION;
    }

    @Override
    public @NotNull String getServerSoftwareVersion0() {
        return Bukkit.getVersion();
    }

    @Override
    public boolean isVersion0(int major, int minor) {
        return Version.isVersion(major, minor);
    }

    @Override
    public boolean isVersion0(int major, int minor, int patch) {
        return Version.isVersion(major, minor, patch);
    }

    @Override
    public boolean isServerThread0() {
        return Bukkit.getServer().isPrimaryThread();
    }

    @Override
    public @NotNull List<@NotNull Player> getConnectedPlayers0() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(BukkitPlayer::new)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull List<@NotNull Player> getConnectedPlayersFromWorld0(@NotNull World holder) {
        return holder.as(org.bukkit.World.class).getPlayers()
                .stream()
                .map(BukkitPlayer::new)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<@NotNull ChannelFuture> getConnections0() {
        return (List<ChannelFuture>) Reflect.fastInvokeResulted(Bukkit.getServer(), "getServer")
                .getFieldResulted(MinecraftServerAccessor.getFieldConnection())
                .getFieldResulted(ServerConnectionListenerAccessor.getFieldChannels())
                .raw();
    }

    @Override
    public @NotNull Sender getConsoleSender0() {
        return new GenericCommandSender(Bukkit.getConsoleSender());
    }

    @Override
    public void shutdown0() {
        Bukkit.shutdown();
    }

    @Override
    public @NotNull ProxyType getProxyType0() {
        // Bukkit#spigot() exists in 1.9.4, verified with https://helpch.at/docs/1.9.4/org/bukkit/Bukkit.html#spigot()
        // Server.Spigot#getPaperConfig() exists in 1.9.4, verified with https://github.com/PaperMC/Paper/blob/ver/1.9.4/Spigot-Server-Patches/0005-Timings-v2.patch#L995
        // Server.Spigot#getConfig() exists in 1.9.4 and resolves to org.spigotmc.SpigotConfig#config in 1.9.4 and latest
        // verified with https://github.com/PaperMC/Paper/blob/ver/1.9.4/Spigot-Server-Patches/0005-Timings-v2.patch#L991
        // and https://github.com/PaperMC/Paper/blob/master/patches/server/0010-Timings-v2.patch#L1727
        if (Reflect.hasMethod(org.bukkit.Server.Spigot.class, "getPaperConfig")) {
            if (Bukkit.spigot().getPaperConfig().getBoolean("settings.velocity-support.enabled", false)) {
                return ProxyType.VELOCITY;
            }
        }
        return Bukkit.spigot().getConfig().getBoolean("settings.bungeecord",false) ? ProxyType.BUNGEE : ProxyType.NONE;
    }

    @Override
    public @NotNull Integer getProtocolVersion0() {
        if (SharedConstantsAccessor.getMethodGetProtocolVersion1() != null) {
            return Reflect.fastInvokeResulted(SharedConstantsAccessor.getMethodGetProtocolVersion1()).as(Integer.class);
        }
        return Reflect.getFieldResulted(Reflect.fastInvoke(Bukkit.getServer(), "getServer"), MinecraftServerAccessor.getFieldStatus())
                .fastInvokeResulted(ServerStatusAccessor.getMethodGetVersion1())
                .fastInvokeResulted(ServerStatus_i_VersionAccessor.getMethodProtocol1())
                .as(Integer.class);
    }

    public static @NotNull String UNSAFE_normalizeSoundKey0(@NotNull String s) {
        // TODO: map legacy <-> flattening conversion
        return UNSAFE_SOUND_CACHE.getOrDefault(s.toUpperCase(Locale.ROOT), s);
    }
}
