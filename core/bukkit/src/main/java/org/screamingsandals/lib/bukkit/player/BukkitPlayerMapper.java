/*
 * Copyright 2022 ScreamingSandals
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
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import java.util.*;

@Service
public class BukkitPlayerMapper extends PlayerMapper {
    public BukkitPlayerMapper() {
        offlinePlayerConverter
                .registerP2W(OfflinePlayer.class, offlinePlayer -> new FinalOfflinePlayerWrapper(offlinePlayer.getUniqueId(), offlinePlayer.getName()))
                .registerP2W(PlayerWrapper.class, playerWrapper -> new FinalOfflinePlayerWrapper(playerWrapper.getUuid(), playerWrapper.getName()))
                .registerW2P(OfflinePlayer.class, offlinePlayerWrapper -> Bukkit.getOfflinePlayer(offlinePlayerWrapper.getUuid()))
                .registerW2P(PlayerWrapper.class, offlinePlayerWrapper -> getPlayer0(offlinePlayerWrapper.getUuid()).orElse(null));

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
    public Optional<PlayerWrapper> getPlayer0(String name) {
        return Optional.ofNullable(Bukkit.getPlayer(name)).map(BukkitEntityPlayer::new);
    }

    @Override
    public Optional<PlayerWrapper> getPlayer0(UUID uuid) {
        return Optional.ofNullable(Bukkit.getPlayer(uuid)).map(BukkitEntityPlayer::new);
    }

    @Override
    protected <T> CommandSenderWrapper wrapSender0(T sender) {
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
    public SenderWrapper getConsoleSender0() {
        return new GenericCommandSender(Bukkit.getConsoleSender());
    }

    @Override
    public @Nullable LocationHolder getBedLocation0(@NotNull OfflinePlayerWrapper playerWrapper) {
        return LocationMapper.resolve(playerWrapper.as(OfflinePlayer.class).getBedSpawnLocation());
    }

    @Override
    public boolean hasPermission0(CommandSenderWrapper wrapper, Permission permission) {
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
    public boolean isPermissionSet0(CommandSenderWrapper wrapper, Permission permission) {
        if (permission instanceof SimplePermission) {
            return wrapper.as(CommandSender.class).isPermissionSet(((SimplePermission) permission).getPermissionString());
        }
        return true;
    }

    @Override
    public boolean isOp0(Operator wrapper) {
        return wrapper.as(CommandSender.class).isOp();
    }

    @Override
    public void setOp0(Operator wrapper, boolean op) {
        wrapper.as(CommandSender.class).setOp(op);
    }

    @Override
    public long getFirstPlayed0(OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).getFirstPlayed();
    }

    @Override
    public long getLastPlayed0(OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).getLastPlayed();
    }

    @Override
    public boolean isBanned0(OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).isBanned();
    }

    @Override
    public boolean isWhitelisted0(OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).isWhitelisted();
    }

    @Override
    public boolean isOnline0(OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).isOnline();
    }

    @Override
    public void setWhitelisted0(OfflinePlayerWrapper playerWrapper, boolean whitelisted) {
        playerWrapper.as(OfflinePlayer.class).setWhitelisted(whitelisted);
    }

    @Override
    public OfflinePlayerWrapper getOfflinePlayer0(UUID uuid) {
        var offPlayer = Bukkit.getOfflinePlayer(uuid);
        if (offPlayer instanceof Player) {
            return new BukkitEntityPlayer((Player) offPlayer);
        }
        return offlinePlayerConverter.convert(offPlayer);
    }

    @Override
    public Optional<OfflinePlayerWrapper> getOfflinePlayer0(String name) {
        var offPlayer = Bukkit.getOfflinePlayer(name);
        if (offPlayer instanceof Player) {
            return Optional.of(new BukkitEntityPlayer((Player) offPlayer));
        }
        return offlinePlayerConverter.convertOptional(offPlayer);
    }

    @Override
    public Optional<PlayerWrapper> getPlayerExact0(String name) {
        return Optional.ofNullable(Bukkit.getPlayerExact(name)).map(BukkitEntityPlayer::new);
    }
}
