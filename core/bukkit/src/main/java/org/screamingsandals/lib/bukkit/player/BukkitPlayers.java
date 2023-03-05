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

package org.screamingsandals.lib.bukkit.player;

import lombok.experimental.ExtensionMethod;
import org.bukkit.*;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.player.*;
import org.screamingsandals.lib.player.OfflinePlayer;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import java.util.*;

@Service
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitPlayers extends Players {
    public BukkitPlayers() {
        offlinePlayerConverter
                .registerP2W(org.bukkit.OfflinePlayer.class, BukkitOfflinePlayer::new)
                .registerP2W(Player.class, playerWrapper -> new BukkitOfflinePlayer(Bukkit.getOfflinePlayer(playerWrapper.getUuid())));

        handConverter
                .registerW2P(EquipmentSlot.class, wrapper -> {
                    if (wrapper == Player.Hand.OFF) {
                        return EquipmentSlot.OFF_HAND;
                    }
                    return EquipmentSlot.HAND;
                })
                .registerP2W(EquipmentSlot.class, hand -> {
                    if (hand == EquipmentSlot.OFF_HAND) {
                        return Player.Hand.OFF;
                    }
                    return Player.Hand.MAIN;
                });
    }

    @Override
    public @Nullable Player getPlayer0(@NotNull String name) {
        return Bukkit.getPlayer(name).mapOrNull(BukkitPlayer::new);
    }

    @Override
    public @Nullable Player getPlayer0(@NotNull UUID uuid) {
        return Bukkit.getPlayer(uuid).mapOrNull(BukkitPlayer::new);
    }

    @Override
    protected <T> @NotNull CommandSender wrapSender0(@NotNull T sender) {
        if (sender instanceof CommandSender) {
            return (CommandSender) sender;
        } else if (sender instanceof OfflinePlayer) {
            return getPlayer0(((OfflinePlayer) sender).getUuid()).orElseThrow();
        } else if (sender instanceof org.bukkit.entity.Player) {
            return new BukkitPlayer((org.bukkit.entity.Player) sender);
        } else if (sender instanceof org.bukkit.OfflinePlayer) {
            return getPlayer0(((org.bukkit.OfflinePlayer) sender).getUniqueId()).orElseThrow();
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
        return Bukkit.getPlayerExact(name).mapOrNull(BukkitPlayer::new);
    }
}
