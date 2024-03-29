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

package org.screamingsandals.lib.plugin;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class holding all instantiable services. You can retrieve them here or via the annotation processor.
 */
@UtilityClass
public class ServiceManager {
    private static final @NotNull List<@NotNull Object> services = new LinkedList<>();

    @ApiStatus.Internal
    public static void putService(@NotNull Object service) {
        if (!services.contains(service)) {
            services.add(service);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull T get(@NotNull Class<T> serviceType) {
        return (T) services.stream().filter(o -> serviceType.isAssignableFrom(o.getClass())).findFirst().orElseThrow();
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getOptional(@NotNull Class<T> serviceType) {
        return (Optional<T>) services.stream().filter(o -> serviceType.isAssignableFrom(o.getClass())).findFirst();
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable T getNullable(@NotNull Class<T> serviceType) {
        return (T) services.stream().filter(o -> serviceType.isAssignableFrom(o.getClass())).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull List<@NotNull T> getAll(@NotNull Class<T> serviceType) {
        return (List<T>) services.stream().filter(o -> serviceType.isAssignableFrom(o.getClass())).collect(Collectors.toList());
    }
}
