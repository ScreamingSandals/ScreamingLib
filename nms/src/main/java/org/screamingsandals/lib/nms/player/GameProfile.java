package org.screamingsandals.lib.nms.player;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.screamingsandals.lib.nms.utils.ClassStorage.NMS.*;
import static org.screamingsandals.lib.reflection.Reflection.*;

@RequiredArgsConstructor
@Getter
public class GameProfile {
    private final @NotNull UUID uuid;
    private final @NotNull String name;
    private final Multimap<String, Property> properties = LinkedHashMultimap.create();

    @SneakyThrows
    public Object toMojang() {
        var gameProfile = AuthlibGameProfile.getConstructor(UUID.class, String.class).newInstance(uuid, name);
        properties.asMap().forEach((key, value) -> value.forEach(property ->
                ((Map<String, Object>) fastInvoke(gameProfile, "getProperties")).put(key, property.getHandle())
        ));
        return gameProfile;
    }

    public Multimap<String, Property> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameProfile)) {
            return false;
        }
        var that = (GameProfile) o;
        return uuid.equals(that.uuid) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name);
    }
}
