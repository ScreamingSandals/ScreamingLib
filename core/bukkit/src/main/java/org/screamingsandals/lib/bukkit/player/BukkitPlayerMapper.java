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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.player.*;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.Operator;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import java.util.*;

@Service
@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
public class BukkitPlayerMapper extends PlayerMapper {
    public BukkitPlayerMapper() {
        offlinePlayerConverter
                .registerP2W(OfflinePlayer.class, offlinePlayer -> new FinalOfflinePlayerWrapper(offlinePlayer.getUniqueId(), offlinePlayer.getName()))
                .registerP2W(PlayerWrapper.class, playerWrapper -> new FinalOfflinePlayerWrapper(playerWrapper.getUuid(), playerWrapper.getName()))
                .registerW2P(OfflinePlayer.class, offlinePlayerWrapper -> Bukkit.getOfflinePlayer(offlinePlayerWrapper.getUuid()))
                .registerW2P(PlayerWrapper.class, offlinePlayerWrapper -> getPlayer0(offlinePlayerWrapper.getUuid()));

        handConverter
                .registerW2P(EquipmentSlot.class, wrapper -> {
                    if (wrapper == PlayerWrapper.Hand.OFF) {
                        return EquipmentSlot.OFF_HAND;
                    }
                    return EquipmentSlot.HAND;
                })
                .registerP2W(EquipmentSlot.class, hand -> {
                    if (hand == EquipmentSlot.OFF_HAND) {
                        return PlayerWrapper.Hand.OFF;
                    }
                    return PlayerWrapper.Hand.MAIN;
                });
    }

    @Override
    public @Nullable PlayerWrapper getPlayer0(@NotNull String name) {
        return Bukkit.getPlayer(name).mapOrNull(BukkitEntityPlayer::new);
    }

    @Override
    public @Nullable PlayerWrapper getPlayer0(@NotNull UUID uuid) {
        return Bukkit.getPlayer(uuid).mapOrNull(BukkitEntityPlayer::new);
    }

    @Override
    protected <T> @NotNull CommandSenderWrapper wrapSender0(@NotNull T sender) {
        if (sender instanceof CommandSenderWrapper) {
            return (CommandSenderWrapper) sender;
        } else if (sender instanceof OfflinePlayerWrapper) {
            return getPlayer0(((OfflinePlayerWrapper) sender).getUuid()).orElseThrow();
        } else if (sender instanceof Player) {
            return new BukkitEntityPlayer((Player) sender);
        } else if (sender instanceof OfflinePlayer) {
            return getPlayer0(((OfflinePlayer) sender).getUniqueId()).orElseThrow();
        } else if (sender instanceof CommandSender) {
            return new GenericCommandSender((CommandSender) sender);
        }
        throw new UnsupportedOperationException("Can't wrap " + sender + " to CommandSenderWrapper");
    }

    @Override
    public @NotNull SenderWrapper getConsoleSender0() {
        return new GenericCommandSender(Bukkit.getConsoleSender());
    }

    @Override
    public @Nullable LocationHolder getBedLocation0(@NotNull OfflinePlayerWrapper playerWrapper) {
        return LocationMapper.resolve(playerWrapper.as(OfflinePlayer.class).getBedSpawnLocation());
    }

    @Override
    public boolean hasPermission0(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission) {
        if (isOp0(wrapper)) {
            return true;
        }

        if (permission instanceof SimplePermission) {
            if (isPermissionSet0(wrapper, permission)) {
                return wrapper.as(CommandSender.class).hasPermission(((SimplePermission) permission).getPermissionString());
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
    public boolean isPermissionSet0(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission) {
        if (permission instanceof SimplePermission) {
            return wrapper.as(CommandSender.class).isPermissionSet(((SimplePermission) permission).getPermissionString());
        }
        return true;
    }

    @Override
    public boolean isOp0(@NotNull Operator wrapper) {
        return wrapper.as(CommandSender.class).isOp();
    }

    @Override
    public void setOp0(@NotNull Operator wrapper, boolean op) {
        wrapper.as(CommandSender.class).setOp(op);
    }

    @Override
    public long getFirstPlayed0(@NotNull OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).getFirstPlayed();
    }

    @Override
    public long getLastPlayed0(@NotNull OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).getLastPlayed();
    }

    @Override
    public boolean isBanned0(@NotNull OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).isBanned();
    }

    @Override
    public boolean isWhitelisted0(@NotNull OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).isWhitelisted();
    }

    @Override
    public boolean isOnline0(@NotNull OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).isOnline();
    }

    @Override
    public void setWhitelisted0(@NotNull OfflinePlayerWrapper playerWrapper, boolean whitelisted) {
        playerWrapper.as(OfflinePlayer.class).setWhitelisted(whitelisted);
    }

    @Override
    public @NotNull OfflinePlayerWrapper getOfflinePlayer0(@NotNull UUID uuid) {
        var offPlayer = Bukkit.getOfflinePlayer(uuid);
        if (offPlayer instanceof Player) {
            return new BukkitEntityPlayer((Player) offPlayer);
        }
        return offlinePlayerConverter.convert(offPlayer);
    }

    @Override
    public @Nullable OfflinePlayerWrapper getOfflinePlayer0(@NotNull String name) {
        var offPlayer = Bukkit.getOfflinePlayer(name);
        if (offPlayer instanceof Player) {
            return new BukkitEntityPlayer((Player) offPlayer);
        }
        return offlinePlayerConverter.convertNullable(offPlayer);
    }

    @Override
    public @Nullable PlayerWrapper getPlayerExact0(@NotNull String name) {
        return Bukkit.getPlayerExact(name).mapOrNull(BukkitEntityPlayer::new);
    }
}
