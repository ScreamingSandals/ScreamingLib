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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ConfigSerializable
public class NPCSkin {
    private static final Map<String, NPCSkin> PLAYER_NAME_SKIN_CACHE = new ConcurrentHashMap<>();
    private static final Map<UUID, NPCSkin> PLAYER_UUID_SKIN_CACHE = new ConcurrentHashMap<>();

    private String value;
    private String signature;

    public static CompletableFuture<@Nullable NPCSkin> retrieveSkin(String playerName) {
        try {
            return retrieveSkin(UUID.fromString(playerName.replaceAll(
                    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                    "$1-$2-$3-$4-$5")));
        } catch (Throwable ignored) {
            if (PLAYER_NAME_SKIN_CACHE.containsKey(playerName)) {
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
                            "$1-$2-$3-$4-$5"))).get();

                    if (skin != null) {
                        PLAYER_NAME_SKIN_CACHE.put(playerName, skin);
                    }

                    return skin;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    return null;
                }
            });
        }
    }

    public static CompletableFuture<@Nullable NPCSkin> retrieveSkin(UUID uuid) {
        if (PLAYER_UUID_SKIN_CACHE.containsKey(uuid)) {
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
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return null;
            }
        });
    }
}
