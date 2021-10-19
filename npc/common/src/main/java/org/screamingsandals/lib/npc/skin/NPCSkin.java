package org.screamingsandals.lib.npc.skin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.GsonUtils;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ConfigSerializable
public class NPCSkin {
    private final String value;
    private final String signature;

    public static Optional<NPCSkin> retrieveSkin(String playerName) {
        try {
            try {
                return retrieveSkin(UUID.fromString(playerName.replaceAll(
                        "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                        "$1-$2-$3-$4-$5")));
            } catch (Throwable ignored) {
                var node = GsonUtils.gson()
                        .fromJson(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName).openStream()), Map.class);

                var userUUID = node.get("id").toString();

                return retrieveSkin(UUID.fromString(userUUID.replaceAll(
                        "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                        "$1-$2-$3-$4-$5")));
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public static Optional<NPCSkin> retrieveSkin(UUID uuid) {
        try {
            var node2 = GsonUtils.gson()
                    .fromJson(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString() + "?unsigned=false").openStream()), Map.class);

            var skinNode = ((List<Map<String, String>>) node2.get("properties"))
                    .stream()
                    .filter(property -> "textures".equalsIgnoreCase(property.get("name")))
                    .findFirst()
                    .orElseThrow();

            return Optional.of(new NPCSkin(skinNode.get("value"), skinNode.get("signature")));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Optional.empty();
    }
}
