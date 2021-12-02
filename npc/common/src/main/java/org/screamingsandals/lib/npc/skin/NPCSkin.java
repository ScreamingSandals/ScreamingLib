package org.screamingsandals.lib.npc.skin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>A class representation of a Minecraft skin.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ConfigSerializable
public class NPCSkin {
    /**
     * <p>A skin cache based on player names.</p>
     */
    private static final Map<String, NPCSkin> PLAYER_NAME_SKIN_CACHE = new ConcurrentHashMap<>();
    /**
     * <p>A skin cache based on player UUID's.</p>
     */
    private static final Map<UUID, NPCSkin> PLAYER_UUID_SKIN_CACHE = new ConcurrentHashMap<>();

    /**
     * <p>The skin value.</p>
     */
    private String value;
    /**
     * <p>The skin signature.</p>
     */
    private String signature;

    /**
     * <p>Attempts to retrieve a player skin from Mojang's API asynchronously.</p>
     * <p>Will use a cached value if it's available.</p>
     *
     * @param playerName the player name
     * @return the {@link CompletableFuture} with the {@link NPCSkin}
     */
    public static CompletableFuture<@Nullable NPCSkin> retrieveSkin(String playerName) {
        return retrieveSkin(playerName, false);
    }

    /**
     * <p>Attempts to retrieve a player skin from Mojang's API asynchronously.</p>
     * <p>Cache lookup will be skipped if force is true.</p>
     *
     * @param playerName the player name
     * @param force should the cache be skipped?
     * @return the {@link CompletableFuture} with the {@link NPCSkin}
     */
    public static CompletableFuture<@Nullable NPCSkin> retrieveSkin(String playerName, boolean force) {
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
                    var node = GsonConfigurationLoader.builder()
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
     * <p>Attempts to retrieve a player skin from Mojang's API asynchronously.</p>
     * <p>Will use a cached value if it's available.</p>
     *
     * @param uuid the player uuid
     * @return the {@link CompletableFuture} with the {@link NPCSkin}
     */
    public static CompletableFuture<@Nullable NPCSkin> retrieveSkin(UUID uuid) {
        return retrieveSkin(uuid, false);
    }

    /**
     * <p>Attempts to retrieve a player skin from Mojang's API asynchronously.</p>
     * <p>Cache lookup will be skipped if force is true.</p>
     *
     * @param uuid the player uuid
     * @param force should the cache be skipped?
     * @return the {@link CompletableFuture} with the {@link NPCSkin}
     */
    public static CompletableFuture<@Nullable NPCSkin> retrieveSkin(UUID uuid, boolean force) {
        if (PLAYER_UUID_SKIN_CACHE.containsKey(uuid) && !force) {
            return CompletableFuture.completedFuture(PLAYER_UUID_SKIN_CACHE.get(uuid));
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                var node2 = GsonConfigurationLoader.builder()
                        .url(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString() + "?unsigned=false"))
                        .build()
                        .load();

                var skinNode =  node2.node("properties")
                        .childrenList()
                        .stream()
                        .filter(property -> "textures".equalsIgnoreCase(property.node("name").getString("")))
                        .findFirst()
                        .orElseThrow();

                var skin = new NPCSkin(skinNode.node("value").getString(), skinNode.node("signature").getString());
                PLAYER_UUID_SKIN_CACHE.put(uuid, skin);

                return skin;
            } catch (Throwable ignored) {
                // failing silently to not annoy the console, because no internet connection for example
            }
            return null;
        });
    }
}
