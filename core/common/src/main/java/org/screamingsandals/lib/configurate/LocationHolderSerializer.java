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

package org.screamingsandals.lib.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.Worlds;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.UUID;

public class LocationHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<Location> {
    public static final @NotNull LocationHolderSerializer INSTANCE = new LocationHolderSerializer();

    private static final @NotNull String X_KEY = "x";
    private static final @NotNull String Y_KEY = "y";
    private static final @NotNull String Z_KEY = "z";
    private static final @NotNull String YAW_KEY = "yaw";
    private static final @NotNull String PITCH_KEY = "pitch";
    private static final @NotNull String WORLD_KEY = "world";

    @Override
    public @NotNull Location deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            var x = node.node(X_KEY).getDouble();
            var y = node.node(Y_KEY).getDouble();
            var z = node.node(Z_KEY).getDouble();
            var yaw = (float) node.node(YAW_KEY).getDouble();
            var pitch = (float) node.node(PITCH_KEY).getDouble();
            var worldName = node.node(WORLD_KEY).getString();
            var world = Worlds.getWorld(worldName);
            if (world == null && worldName != null) {
                world = Worlds.getWorld(UUID.fromString(worldName));
            }
            if (world == null) {
                throw new SerializationException("Unknown world: " + worldName);
            }
            return new Location(x, y, z, yaw, pitch, world);
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable Location obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(X_KEY).set(obj.getX());
        node.node(Y_KEY).set(obj.getY());
        node.node(Z_KEY).set(obj.getZ());
        node.node(YAW_KEY).set(obj.getYaw());
        node.node(PITCH_KEY).set(obj.getPitch());
        node.node(WORLD_KEY).set(obj.getWorld().getName());
    }
}
