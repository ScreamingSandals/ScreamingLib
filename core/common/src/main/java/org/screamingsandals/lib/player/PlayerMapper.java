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

package org.screamingsandals.lib.player;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.Operator;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.*;

@AbstractService
public abstract class PlayerMapper {
    protected final @NotNull BidirectionalConverter<OfflinePlayerWrapper> offlinePlayerConverter = BidirectionalConverter.build();
    protected final @NotNull BidirectionalConverter<PlayerWrapper> specialPlayerConverter = BidirectionalConverter.build();
    protected final @NotNull BidirectionalConverter<PlayerWrapper.Hand> handConverter = BidirectionalConverter.build();
    private static @Nullable PlayerMapper playerMapper;

    @ApiStatus.Internal
    public PlayerMapper() {
        if (playerMapper != null) {
            throw new UnsupportedOperationException("PlayerMapper is already initialized.");
        }
        playerMapper = this;
    }

    @OnPostConstruct
    public void postConstruct() {
        offlinePlayerConverter
                .registerP2W(UUID.class, uuid -> new FinalOfflinePlayerWrapper(uuid, null))
                .registerW2P(UUID.class, OfflinePlayerWrapper::getUuid)
                .registerP2W(OfflinePlayerWrapper.class, e -> e);
        handConverter
                .registerP2W(PlayerWrapper.Hand.class, e -> e);
    }

    public static <T> @NotNull PlayerWrapper wrapPlayer(@NotNull T player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        try {
            var sender = playerMapper.wrapSender0(player);

            if (sender instanceof PlayerWrapper) {
                return (PlayerWrapper) sender;
            }
        } catch (UnsupportedOperationException ignored) {}

        return playerMapper.specialPlayerConverter.convert(player);
    }

    public static <T> OfflinePlayerWrapper wrapOfflinePlayer(@NotNull T player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.offlinePlayerConverter.convert(player);
    }

    public static <T> @NotNull CommandSenderWrapper wrapSender(@NotNull T sender) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.wrapSender0(sender);
    }

    public static <T> PlayerWrapper.@NotNull Hand wrapHand(@NotNull T hand) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.handConverter.convert(hand);
    }

    public static <T> PlayerWrapper.@Nullable Hand resolveHand(@Nullable T hand) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        if (hand == null) {
            return null;
        }
        return playerMapper.handConverter.convertNullable(hand);
    }

    public static <T> T convertHand(PlayerWrapper.@NotNull Hand hand, @NotNull Class<T> type) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.handConverter.convert(hand, type);
    }

    public static <T> T convertOfflinePlayer(@NotNull OfflinePlayerWrapper player, @NotNull Class<T> type) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.offlinePlayerConverter.convert(player, type);
    }

    public static @Nullable LocationHolder getBedLocation(@NotNull OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getBedLocation0(wrapper);
    }

    @Contract("null -> null")
    public static @Nullable PlayerWrapper getPlayer(@Nullable String name) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        if (name == null) {
            return null;
        }
        return playerMapper.getPlayer0(name);
    }

    public abstract @Nullable PlayerWrapper getPlayer0(@NotNull String name);

    @Contract("null -> null")
    public static @Nullable PlayerWrapper getPlayer(@Nullable UUID uuid) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        if (uuid == null) {
            return null;
        }
        return playerMapper.getPlayer0(uuid);
    }

    public abstract @Nullable PlayerWrapper getPlayer0(@NotNull UUID uuid);

    @Contract("null -> null")
    public static @Nullable PlayerWrapper getPlayerExact(@Nullable String name) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        if (name == null) {
            return null;
        }
        return playerMapper.getPlayerExact0(name);
    }

    public static @NotNull SenderWrapper getConsoleSender() {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getConsoleSender0();
    }

    public static boolean hasPermission(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.hasPermission0(wrapper, permission);
    }

    public static boolean isPermissionSet(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isPermissionSet0(wrapper, permission);
    }

    public static boolean isOp(@NotNull Operator wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isOp0(wrapper);
    }

    public static void setOp(@NotNull Operator wrapper, boolean op) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setOp0(wrapper, op);
    }

    public static long getFirstPlayed(@NotNull OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getFirstPlayed0(wrapper);
    }

    public static long getLastPlayed(@NotNull OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getLastPlayed0(wrapper);
    }

    public static boolean isBanned(@NotNull OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isBanned0(wrapper);
    }

    public static boolean isWhitelisted(@NotNull OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isWhitelisted0(wrapper);
    }

    public static boolean isOnline(@NotNull OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isOnline0(wrapper);
    }

    public static void setWhitelisted(@NotNull OfflinePlayerWrapper wrapper, boolean whitelisted) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setWhitelisted0(wrapper, whitelisted);
    }

    public static @NotNull OfflinePlayerWrapper getOfflinePlayer(@NotNull UUID uuid) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getOfflinePlayer0(uuid);
    }

    /**
     * This method may involve a blocking web request to get the UUID for the given name.
     *
     * @param name Name of the player
     * @return the offline player or null if not found
     * @deprecated see {@link PlayerMapper#getOfflinePlayer(UUID)}
     */
    @Deprecated
    @Contract("null -> null")
    public static @Nullable OfflinePlayerWrapper getOfflinePlayer(@Nullable String name) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        if (name == null) {
            return null;
        }
        return playerMapper.getOfflinePlayer0(name);
    }

    public static @NotNull BidirectionalConverter<PlayerWrapper> UNSAFE_getPlayerConverter() {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        
        return playerMapper.specialPlayerConverter;
    }

    // abstract methods for implementations

    protected abstract <T> @NotNull CommandSenderWrapper wrapSender0(@NotNull T sender);

    public abstract @NotNull SenderWrapper getConsoleSender0();

    public abstract @Nullable LocationHolder getBedLocation0(@NotNull OfflinePlayerWrapper playerWrapper);

    public abstract boolean hasPermission0(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission);

    public abstract boolean isPermissionSet0(@NotNull CommandSenderWrapper wrapper, @NotNull Permission permission);

    public abstract boolean isOp0(@NotNull Operator wrapper);

    public abstract void setOp0(@NotNull Operator wrapper, boolean op);

    public abstract long getFirstPlayed0(@NotNull OfflinePlayerWrapper playerWrapper);

    public abstract long getLastPlayed0(@NotNull OfflinePlayerWrapper playerWrapper);

    public abstract boolean isBanned0(@NotNull OfflinePlayerWrapper playerWrapper);

    public abstract boolean isWhitelisted0(@NotNull OfflinePlayerWrapper playerWrapper);

    public abstract boolean isOnline0(@NotNull OfflinePlayerWrapper playerWrapper);

    public abstract void setWhitelisted0(@NotNull OfflinePlayerWrapper playerWrapper, boolean whitelisted);

    public abstract @NotNull OfflinePlayerWrapper getOfflinePlayer0(@NotNull UUID uuid);

    public abstract @Nullable OfflinePlayerWrapper getOfflinePlayer0(@NotNull String name);

    public abstract @Nullable PlayerWrapper getPlayerExact0(@NotNull String name);
}
