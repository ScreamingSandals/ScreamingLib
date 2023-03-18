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

package org.screamingsandals.lib.bukkit.entity.pose;

import org.bukkit.entity.Pose;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.pose.EntityPose;
import org.screamingsandals.lib.entity.pose.EntityPoseRegistry;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitEntityPoseRegistry extends EntityPoseRegistry {
    private final boolean HAS_POSE = Reflect.has("org.bukkit.entity.Pose");

    public BukkitEntityPoseRegistry() {
        if (HAS_POSE) {
            specialType(Pose.class, BukkitEntityPose::new);
        }
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull EntityPose> getRegistryItemStream0() {
        if (HAS_POSE) {
            return new SimpleRegistryItemStream<>(
                    () -> Arrays.stream(Pose.values()),
                    BukkitEntityPose::new,
                    pose -> ResourceLocation.of(pose.name()),
                    (pose, literal) -> pose.name().toLowerCase(Locale.ROOT).contains(literal),
                    (pose, namespace) -> "minecraft".equals(namespace),
                    List.of()
            );
        } else {
            return SimpleRegistryItemStream.createDummy();
        }
    }

    @Override
    protected @Nullable EntityPose resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (HAS_POSE) {
            if (!"minecraft".equals(location.namespace())) {
                return null;
            }

            try {
                var enumName = location.path();

                if ("crouching".equals(location.path())) {
                    enumName = "sneaking";
                }

                var value = Pose.valueOf(enumName.toUpperCase(Locale.ROOT));
                return new BukkitEntityPose(value);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return null;
    }
}
