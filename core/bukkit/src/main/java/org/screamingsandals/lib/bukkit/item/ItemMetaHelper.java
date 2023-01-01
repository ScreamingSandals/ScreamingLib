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

package org.screamingsandals.lib.bukkit.item;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.metadata.MetadataValuesRemapper;
import org.screamingsandals.lib.metadata.MetadataCollectionKey;
import org.screamingsandals.lib.metadata.MetadataKey;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemMetaHelper {
    private static final Map<String, String> NBT_TO_BUKKIT_METHODS = Map.ofEntries(
            // PotionMeta
            Map.entry("Potion", "BasePotionData"),
            Map.entry("CustomPotionColor", "Color"),
            Map.entry("CustomPotionEffects", "CustomEffects"),
            // KnowledgeBookMeta
            Map.entry("Recipes", "Recipes"),
            // FireworkMeta
            Map.entry("Fireworks.Explosions", "Effects"),
            Map.entry("Fireworks.Flight", "Power"),
            // FireworkEffectMeta
            Map.entry("Explosion", "Effect"),
            // LeatherArmorMeta
            Map.entry("display.color", "Color"),
            // SkullMeta
            Map.entry("SkullOwner", "Owner")
            // TODO: map the rest
    );

    public static boolean supportsMetadata(ItemMeta meta, MetadataKey<?> key) {
        var name = NBT_TO_BUKKIT_METHODS.getOrDefault(key.getKey(), key.getKey().replace(".", ""));

        return Reflect.getMethod(meta, "get" + name).isPresent();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T getMetadata(ItemMeta meta, MetadataKey<T> key) {
        var name = NBT_TO_BUKKIT_METHODS.getOrDefault(key.getKey(), key.getKey().replace(".", ""));

        var hasMethod = Reflect.getMethod(meta, "has" + name);
        var getMethod = Reflect.getMethod(meta, "get" + name);

        if (hasMethod.isPresent()) {
            if (!(boolean) hasMethod.invoke()) {
                return null;
            }
        }

        if (getMethod.isPresent()) {
            return (T) MetadataValuesRemapper.remapToWrapper(getMethod.invoke(), key.getType());
        }

        // TODO: look for raw nbt using CraftMetaItem
        return null;
    }

    public static boolean supportsMetadata(ItemMeta meta, MetadataCollectionKey<?> key) {
        var name = NBT_TO_BUKKIT_METHODS.getOrDefault(key.getKey(), key.getKey().replace(".", ""));

        return Reflect.getMethod(meta, "get" + name).isPresent();
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> getMetadata(ItemMeta meta, MetadataCollectionKey<T> key) {
        var name = NBT_TO_BUKKIT_METHODS.getOrDefault(key.getKey(), key.getKey().replace(".", ""));

        var hasMethod = Reflect.getMethod(meta, "has" + name);
        var getMethod = Reflect.getMethod(meta, "get" + name);

        if (hasMethod.isPresent()) {
            if (!(boolean) hasMethod.invoke()) {
                return null;
            }
        }

        if (getMethod.isPresent()) {
            return (Collection<T>) getMethod.invokeResulted()
                    .as(Collection.class)
                    .stream()
                    .map(o -> (T) MetadataValuesRemapper.remapToWrapper(o, key.getComponentType()))
                    .collect(Collectors.toList());
        }

        // TODO: look for raw nbt using CraftMetaItem
        return null;
    }

    public static <T> void setMetadata(ItemMeta meta, MetadataKey<T> key, Object value) {
        var name = NBT_TO_BUKKIT_METHODS.getOrDefault(key.getKey(), key.getKey().replace(".", ""));

        var setMethod = Reflect.getMethodsCalled(meta, "set" + name, 1);

        if (!setMethod.isEmpty()) {
            for (var m : setMethod) {
                try {
                    m.invoke(MetadataValuesRemapper.remapToPlatform(value, m.getMethod().getParameterTypes()[0]));
                    return;
                } catch (Throwable ignored) {
                    // continue
                }
            }
        }

        // TODO: save as raw nbt using CraftMetaItem
    }

    public static <T> void setMetadata(ItemMeta meta, MetadataCollectionKey<T> key, Collection<T> value) {
        var name = NBT_TO_BUKKIT_METHODS.getOrDefault(key.getKey(), key.getKey().replace(".", ""));

        var setMethod = Reflect.getMethodsCalled(meta, "set" + name, 1);

        if (!setMethod.isEmpty()) {
            for (var m : setMethod) {
                try {
                    if (!Collection.class.isAssignableFrom(m.getMethod().getParameterTypes()[0])) {
                        return;
                    }
                    m.invoke(value.stream().map(o -> MetadataValuesRemapper.remapToPlatform(o, ((Class<?>) ((ParameterizedType) m.getMethod().getParameters()[0].getParameterizedType()).getActualTypeArguments()[0]))).collect(Collectors.toList()));
                    return;
                } catch (Throwable ignored) {
                    // continue
                }
            }
        }

        var clearMethod = Reflect.getMethod(meta, "clear" + name);
        var addMethod = Reflect.getMethodsCalled(meta, "add" + name.substring(0, name.length() - 1), 1);
        if (addMethod.isEmpty()) {
            // Ah, addCustomEffect
            addMethod = Reflect.getMethodsCalled(meta, "add" + name.substring(0, name.length() - 1), 2);
        }
        if (clearMethod.isPresent() && !addMethod.isEmpty()) {
            clearMethod.invoke();
            for (var m : addMethod) {
                try {
                    if (!Collection.class.isAssignableFrom(m.getMethod().getParameterTypes()[0])) {
                        return;
                    }
                    m.invoke(value.stream().map(o -> MetadataValuesRemapper.remapToPlatform(o, ((Class<?>) ((ParameterizedType) m.getMethod().getParameters()[0].getParameterizedType()).getActualTypeArguments()[0]))).collect(Collectors.toList()));
                    return;
                } catch (Throwable ignored) {
                    // continue
                }
            }
        }

        // TODO: save as raw nbt using CraftMetaItem
    }

    public static <T> void addMetadata(ItemMeta meta, MetadataCollectionKey<T> key, T value) {
        var name = NBT_TO_BUKKIT_METHODS.getOrDefault(key.getKey(), key.getKey().replace(".", ""));

        var hasMethod = Reflect.getMethod(meta, "has" + name);
        var getMethod = Reflect.getMethod(meta, "get" + name);

        List orig = List.of();
        if (hasMethod.isPresent()) {
            if ((boolean) hasMethod.invoke()) {
                orig = getMethod.invokeResulted().as(List.class);
            }
        }

        var setMethod = Reflect.getMethodsCalled(meta, "set" + name, 1);

        if (!setMethod.isEmpty()) {
            for (var m : setMethod) {
                try {
                    if (!Collection.class.isAssignableFrom(m.getMethod().getParameterTypes()[0])) {
                        return;
                    }
                    var orig2 = new ArrayList<>(orig);
                    orig2.add(MetadataValuesRemapper.remapToPlatform(value, ((Class<?>) ((ParameterizedType) m.getMethod().getParameters()[0].getParameterizedType()).getActualTypeArguments()[0])));
                    m.invoke(orig2);
                    return;
                } catch (Throwable ignored) {
                    // continue
                }
            }
        }

        var addMethod = Reflect.getMethodsCalled(meta, "add" + name.substring(0, name.length() - 1), 1);
        if (addMethod.isEmpty()) {
            // Ah, addCustomEffect
            addMethod = Reflect.getMethodsCalled(meta, "add" + name.substring(0, name.length() - 1), 2);
        }
        if (!addMethod.isEmpty()) {
            for (var m : addMethod) {
                try {
                    if (m.getMethod().getParameters().length == 2) {
                        m.invoke(MetadataValuesRemapper.remapToPlatform(value, m.getMethod().getParameterTypes()[0]), true);
                    } else {
                        m.invoke(MetadataValuesRemapper.remapToPlatform(value, m.getMethod().getParameterTypes()[0]));
                    }
                    return;
                } catch (Throwable ignored) {
                    // continue
                }
            }
        }

        // TODO: save as raw nbt using CraftMetaItem
    }


}
