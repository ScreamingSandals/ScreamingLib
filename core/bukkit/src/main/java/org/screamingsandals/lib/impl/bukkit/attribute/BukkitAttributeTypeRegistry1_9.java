/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.attribute;

import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.nms.accessors.AttributeAccessor;
import org.screamingsandals.lib.impl.nms.accessors.HorseAccessor;
import org.screamingsandals.lib.impl.nms.accessors.AttributesAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ZombieAccessor;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class BukkitAttributeTypeRegistry1_9 extends BukkitAttributeTypeRegistry {
    private final @NotNull Map<@NotNull Attribute, Object> bukkitAttributeToVanillaAttribute = new HashMap<>();

    private @Nullable Attribute genericArmorToughness;

    public BukkitAttributeTypeRegistry1_9() {
        specialType(Attribute.class, a -> new BukkitAttributeType1_9(a, bukkitAttributeToVanillaAttribute.get(a)));

        if (BukkitFeature.ATTRIBUTE_ARMOR_TOUGHNESS_VANILLA.isSupported() && !BukkitFeature.ATTRIBUTE_ARMOR_TOUGHNESS.isSupported()) { // 1.9.1 - 1.11.2 (late builds)
            // I am sorry Java, but I had no other choice (manually creating the instance will work at least for obtaining AttributeInstance, so we won't inject it into values to not confuse other plugins)
            // md_5 has just completely forgotten about this type for more than a year (and a two major versions)
            // TODO: check if this shit actually works or not
            var backport = Reflect.newEnumValue(Attribute.class,"GENERIC_ARMOR_TOUGHNESS", Attribute.values().length);
            if (backport != null) {
                genericArmorToughness = backport;
            }
        }

        put("GENERIC_MAX_HEALTH", AttributesAccessor.FIELD_MAX_HEALTH.get());
        put("GENERIC_FOLLOW_RANGE", AttributesAccessor.FIELD_FOLLOW_RANGE.get());
        put("GENERIC_KNOCKBACK_RESISTANCE", AttributesAccessor.FIELD_KNOCKBACK_RESISTANCE.get());
        put("GENERIC_MOVEMENT_SPEED", AttributesAccessor.FIELD_MOVEMENT_SPEED.get());
        put("GENERIC_FLYING_SPEED", AttributesAccessor.FIELD_FLYING_SPEED.get());
        put("GENERIC_ATTACK_DAMAGE", AttributesAccessor.FIELD_ATTACK_DAMAGE.get());
        put("GENERIC_ATTACK_SPEED", AttributesAccessor.FIELD_ATTACK_SPEED.get());
        put("GENERIC_ARMOR", AttributesAccessor.FIELD_ARMOR.get());
        put("GENERIC_ARMOR_TOUGHNESS", AttributesAccessor.FIELD_ARMOR_TOUGHNESS.get(), genericArmorToughness);
        put("GENERIC_LUCK", AttributesAccessor.FIELD_LUCK.get());
        put("HORSE_JUMP_STRENGTH", HorseAccessor.FIELD_ATTRIBUTE_JUMP_STRENGTH.get());
        put("ZOMBIE_SPAWN_REINFORCEMENTS", ZombieAccessor.FIELD_SPAWN_REINFORCEMENTS_CHANCE.get());
    }

    private void put(@NotNull String attributeTypeName, @Nullable Object attribute) {
        put(attributeTypeName, attribute, null);
    }

    private void put(@NotNull String attributeTypeName, @Nullable Object attribute, @Nullable Attribute artificialInstance) {
        try {
            if (attribute != null && AttributeAccessor.TYPE.get() != null && AttributeAccessor.TYPE.get().isInstance(attribute)) {
                var attributeType = artificialInstance != null ? artificialInstance : Attribute.valueOf(attributeTypeName);
                bukkitAttributeToVanillaAttribute.put(attributeType, attribute);
            }
        } catch (IllegalArgumentException ignored) {
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
                a -> new BukkitAttributeType1_9(a, bukkitAttributeToVanillaAttribute.get(a)),
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
            return new BukkitAttributeType1_9(value, bukkitAttributeToVanillaAttribute.get(value));
        } catch (IllegalArgumentException ignored) {
        }

        if (genericArmorToughness != null && "generic.armor_toughness".equals(location.path())) {
            return new BukkitAttributeType1_9(genericArmorToughness, bukkitAttributeToVanillaAttribute.get(genericArmorToughness));
        }

        return null;
    }
}
