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

package org.screamingsandals.lib.npc.skin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.config.JsonConfigurationLoaderBuilderSupplier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class representation of a Minecraft skin.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ConfigSerializable
public class NPCSkin {
    /**
     * A skin cache based on player names.
     */
    private static final @NotNull Map<@NotNull String, NPCSkin> PLAYER_NAME_SKIN_CACHE = new ConcurrentHashMap<>();
    /**
     * A skin cache based on player UUID's.
     */
    private static final @NotNull Map<@NotNull UUID, NPCSkin> PLAYER_UUID_SKIN_CACHE = new ConcurrentHashMap<>();

    /**
     * The skin value.
     */
    private @NotNull String value;
    /**
     * The skin signature.
     */
    private @NotNull String signature;

    /**
     * Attempts to retrieve a player skin from Mojang's API asynchronously.
     * Will use a cached value if it's available.
     *
     * @param playerName the player name
     * @return the {@link CompletableFuture} with the {@link NPCSkin}
     */
    public static @NotNull CompletableFuture<@Nullable NPCSkin> retrieveSkin(@NotNull String playerName) {
        return retrieveSkin(playerName, false);
    }

    /**
     * Attempts to retrieve a player skin from Mojang's API asynchronously.
     * Cache lookup will be skipped if force is true.
     *
     * @param playerName the player name
     * @param force should the cache be skipped?
     * @return the {@link CompletableFuture} with the {@link NPCSkin}
     */
    public static @NotNull CompletableFuture<@Nullable NPCSkin> retrieveSkin(@NotNull String playerName, boolean force) {
        try {
            return retrieveSkin(UUID.fromString(playerName.replaceAll(
                    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                    "$1-$2-$3-$4-$5")), force);
        } catch (Throwable ignored) {
            if (PLAYER_NAME_SKIN_CACHE.containsKey(playerName) && !force) {
                return CompletableFuture.completedFuture(PLAYER_NAME_SKIN_CACHE.get(playerName));
            }

            return CompletableFuture.supplyAsync(() -> {
                try {
                    var node = JsonConfigurationLoaderBuilderSupplier.INSTANCE
                            .get()
                            .url(new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName))
                            .build()
                            .load();

                    var userUUID = node.node("id").getString("");

                    var skin = retrieveSkin(UUID.fromString(userUUID.replaceAll(
                            "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                            "$1-$2-$3-$4-$5")), force).get();

                    if (skin != null) {
                        PLAYER_NAME_SKIN_CACHE.put(playerName, skin);
                    }

                    return skin;
                } catch (Throwable ignored2) {
                    // failing silently to not annoy the console, because no internet connection for example
                }
                return null;
            });
        }
    }

    /**
     * Attempts to retrieve a player skin from Mojang's API asynchronously.
     * Will use a cached value if it's available.
     *
     * @param uuid the player uuid
     * @return the {@link CompletableFuture} with the {@link NPCSkin}
     */
    public static @NotNull CompletableFuture<@Nullable NPCSkin> retrieveSkin(@NotNull UUID uuid) {
        return retrieveSkin(uuid, false);
    }

    /**
     * Attempts to retrieve a player skin from Mojang's API asynchronously.
     * Cache lookup will be skipped if force is true.
     *
     * @param uuid the player uuid
     * @param force should the cache be skipped?
     * @return the {@link CompletableFuture} with the {@link NPCSkin}
     */
    public static @NotNull CompletableFuture<@Nullable NPCSkin> retrieveSkin(@NotNull UUID uuid, boolean force) {
        if (PLAYER_UUID_SKIN_CACHE.containsKey(uuid) && !force) {
            return CompletableFuture.completedFuture(PLAYER_UUID_SKIN_CACHE.get(uuid));
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                var node2 = JsonConfigurationLoaderBuilderSupplier.INSTANCE
                        .get()
                        .url(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString() + "?unsigned=false"))
                        .build()
                        .load();

                var skinNode =  node2.node("properties")
                        .childrenList()
                        .stream()
                        .filter(property -> "textures".equalsIgnoreCase(property.node("name").getString("")))
                        .findFirst()
                        .orElseThrow();

                var skin = new NPCSkin(Objects.requireNonNull(skinNode.node("value").getString()), Objects.requireNonNull(skinNode.node("signature").getString()));
                PLAYER_UUID_SKIN_CACHE.put(uuid, skin);

                return skin;
            } catch (Throwable ignored) {
                // failing silently to not annoy the console, because no internet connection for example
            }
            return null;
        });
    }
}
