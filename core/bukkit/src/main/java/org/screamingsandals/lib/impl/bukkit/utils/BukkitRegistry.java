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

package org.screamingsandals.lib.impl.bukkit.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class BukkitRegistry {
    public static <T extends Keyed, I extends RegistryItem> @NotNull RegistryItemStream<@NotNull I> registryStream(
            @NotNull Registry<T> registry,
            @NotNull Function<@NotNull T, @NotNull I> constructor
    ) {
        return registryStream(registry, constructor, null);
    }

    public static <T extends Keyed, I extends RegistryItem> @NotNull RegistryItemStream<@NotNull I> registryStream(
            @NotNull Registry<T> registry,
            @NotNull Function<@NotNull T, @NotNull I> constructor,
            @Nullable Predicate<@NotNull T> filter
    ) {
        @NotNull Supplier<@NotNull Stream<@NotNull T>> streamSupplier;

        if (filter != null) {
            streamSupplier = () -> stream(registry).filter(filter);
        } else {
            streamSupplier = () -> stream(registry);
        }

        return new SimpleRegistryItemStream<>(
                streamSupplier,
                constructor,
                BukkitRegistry::resourceLocation,
                (item, literal) -> item.getKey().getKey().contains(literal),
                (item, namespace) -> item.getKey().getNamespace().equals(namespace),
                List.of()
        );
    }

    public static <T extends Keyed, I extends RegistryItem> @Nullable I tryObtainItem(
            @NotNull Registry<T> registry,
            @NotNull Function<@NotNull T, @Nullable I> constructor,
            @NotNull ResourceLocation location
    ) {
        return tryObtainItem(registry, constructor, location, null);
    }

    public static <T extends Keyed, I extends RegistryItem> @Nullable I tryObtainItem(
            @NotNull Registry<T> registry,
            @NotNull Function<@NotNull T, @Nullable I> constructor,
            @NotNull ResourceLocation location,
            @Nullable Predicate<@NotNull T> filter
    ) {
        var type = registry.get(new NamespacedKey(location.namespace(), location.path()));
        if (type != null && (filter == null || filter.test(type))) {
            return constructor.apply(type);
        }
        return null;
    }

    public static <T extends Keyed> @NotNull Stream<T> stream(@NotNull Registry<T> registry) {
        if (BukkitFeature.REGISTRY_STREAM_METHOD.isSupported()) {
            return registry.stream();
        } else {
            return StreamSupport.stream(registry.spliterator(), false);
        }
    }

    public static ResourceLocation resourceLocation(@NotNull Keyed keyed) {
        var key = keyed.getKey();
        return ResourceLocation.of(key.getNamespace(), key.getKey());
    }
}
