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

package org.screamingsandals.lib.bukkit.attribute;

import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.bukkit.utils.Version;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class BukkitAttributeTypeRegistry1_9 extends BukkitAttributeTypeRegistry {
    private @Nullable Attribute genericArmorToughness;

    public BukkitAttributeTypeRegistry1_9() {
        specialType(Attribute.class, BukkitAttributeType1_9::new);

        if (Version.isVersion(1, 9, 1) && Reflect.getField(Attribute.class, "GENERIC_ARMOR_TOUGHNESS") == null) { // 1.9.1 - 1.11.2 (late builds)
            // I am sorry Java, but I had no other choice (manually creating the instance will work at least for obtaining AttributeInstance, so we won't inject it into values to not confuse other plugins)
            // md_5 has just completely forgotten about this type for more than a year (and a two major versions)
            // TODO: check if this shit actually works or not
            var backport = Reflect.newEnumValue(Attribute.class,"GENERIC_ARMOR_TOUGHNESS", Attribute.values().length);
            if (backport != null) {
                genericArmorToughness = backport;
            }
        }
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull AttributeType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> {
                    if (genericArmorToughness == null) {
                        return Arrays.stream(Attribute.values());
                    } else {
                        return Stream.concat(Arrays.stream(Attribute.values()), Stream.of(genericArmorToughness));
                    }
                },
                BukkitAttributeType1_9::new,
                BukkitAttributeType1_9::getLocation,
                (attributeType, literal) -> BukkitAttributeType1_9.getLocation(attributeType).path().contains(literal),
                (attributeType, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }

    @Override
    protected @Nullable AttributeType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        try {
            var path = location.path().toUpperCase(Locale.ROOT).replace(".", "_");
            var value = Attribute.valueOf(path);
            return new BukkitAttributeType1_9(value);
        } catch (IllegalArgumentException ignored) {
        }

        if (genericArmorToughness != null && "generic.armor_toughness".equals(location.path())) {
            return new BukkitAttributeType1_9(genericArmorToughness);
        }

        return null;
    }
}
