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

package org.screamingsandals.lib.impl.bukkit.player;

import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ConnectionAccessor;
import org.screamingsandals.lib.nms.accessors.ServerGamePacketListenerImplAccessor;
import org.screamingsandals.lib.nms.accessors.ServerPlayerAccessor;
import org.screamingsandals.lib.player.*;
import org.screamingsandals.lib.player.OfflinePlayer;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.*;

@Service
public class BukkitPlayers extends Players {
    protected final @NotNull WeakHashMap<org.bukkit.entity.@NotNull Player, Channel> channelCache = new WeakHashMap<>();

    public BukkitPlayers() {
        offlinePlayerConverter
                .registerP2W(org.bukkit.OfflinePlayer.class, BukkitOfflinePlayer::new)
                .registerP2W(Player.class, playerWrapper -> new BukkitOfflinePlayer(Bukkit.getOfflinePlayer(playerWrapper.getUuid())));
    }

    @OnPostEnable
    public void onPostEnable(@NotNull Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            public void onQuit(@NotNull PlayerQuitEvent event) {
                channelCache.remove(event.getPlayer());
            }
        }, plugin);
    }

    @OnPreDisable
    public void onPreDisable() {
        Bukkit.getOnlinePlayers().forEach(channelCache::remove);
    }


    @Override
    public @Nullable Player getPlayer0(@NotNull String name) {
        var bukkitPlayer = Bukkit.getPlayer(name);
        return bukkitPlayer == null ? null : new BukkitPlayer(bukkitPlayer);
    }

    @Override
    public @Nullable Player getPlayer0(@NotNull UUID uuid) {
        var bukkitPlayer = Bukkit.getPlayer(uuid);
        return bukkitPlayer == null ? null : new BukkitPlayer(bukkitPlayer);
    }

    @Override
    protected <T> @NotNull CommandSender wrapSender0(@NotNull T sender) {
        if (sender instanceof CommandSender) {
            return (CommandSender) sender;
        } else if (sender instanceof OfflinePlayer) {
            return Preconditions.checkNotNull(getPlayer0(((OfflinePlayer) sender).getUuid()));
        } else if (sender instanceof org.bukkit.entity.Player) {
            return new BukkitPlayer((org.bukkit.entity.Player) sender);
        } else if (sender instanceof org.bukkit.OfflinePlayer) {
            return Preconditions.checkNotNull(getPlayer0(((org.bukkit.OfflinePlayer) sender).getUniqueId()));
        } else if (sender instanceof org.bukkit.command.CommandSender) {
            return new GenericCommandSender((org.bukkit.command.CommandSender) sender);
        }
        throw new UnsupportedOperationException("Can't wrap " + sender + " to CommandSenderWrapper");
    }

    @Override
    public boolean hasPermission0(@NotNull CommandSender wrapper, @NotNull Permission permission) {
        if (wrapper.isOp()) {
            return true;
        }

        if (permission instanceof SimplePermission) {
            if (isPermissionSet0(wrapper, permission)) {
                return wrapper.as(org.bukkit.command.CommandSender.class).hasPermission(((SimplePermission) permission).getPermissionString());
            } else {
                return ((SimplePermission) permission).isDefaultAllowed();
            }
        } else if (permission instanceof AndPermission) {
            return ((AndPermission) permission).getPermissions().stream().allMatch(permission1 -> hasPermission0(wrapper, permission1));
        } else if (permission instanceof OrPermission) {
            return ((OrPermission) permission).getPermissions().stream().anyMatch(permission1 -> hasPermission0(wrapper, permission1));
        } else if (permission instanceof PredicatePermission) {
            return permission.hasPermission(wrapper);
        }
        return false;
    }

    @Override
    public boolean isPermissionSet0(@NotNull CommandSender wrapper, @NotNull Permission permission) {
        if (permission instanceof SimplePermission) {
            return wrapper.as(org.bukkit.command.CommandSender.class).isPermissionSet(((SimplePermission) permission).getPermissionString());
        }
        return true;
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer0(@NotNull UUID uuid) {
        var offPlayer = Bukkit.getOfflinePlayer(uuid);
        if (offPlayer instanceof org.bukkit.entity.Player) {
            return new BukkitPlayer((org.bukkit.entity.Player) offPlayer);
        }
        return new BukkitOfflinePlayer(offPlayer);
    }

    @Override
    public @Nullable OfflinePlayer getOfflinePlayer0(@NotNull String name) {
        var offPlayer = Bukkit.getOfflinePlayer(name);
        if (offPlayer instanceof org.bukkit.entity.Player) {
            return new BukkitPlayer((org.bukkit.entity.Player) offPlayer);
        }
        return new BukkitOfflinePlayer(offPlayer);
    }

    @Override
    public @Nullable Player getPlayerExact0(@NotNull String name) {
        var bukkitPlayer = Bukkit.getPlayerExact(name);
        return bukkitPlayer == null ? null : new BukkitPlayer(bukkitPlayer);
    }

    @Override
    protected Channel getNettyChannel0(Player playerWrapper) {
        final var bukkitPlayer = playerWrapper.as(org.bukkit.entity.Player.class);
        final var cachedChannel = channelCache.get(bukkitPlayer);

        if (cachedChannel != null) {
            return cachedChannel;
        }

        final var channel = (Channel) Reflect.getFieldResulted(ClassStorage.getHandle(bukkitPlayer), ServerPlayerAccessor.getFieldConnection())
                .getFieldResulted(ServerGamePacketListenerImplAccessor.getFieldConnection())
                .getFieldResulted(ConnectionAccessor.getFieldChannel())
                .raw();

        channelCache.put(bukkitPlayer, channel);
        return channel;
    }
}
