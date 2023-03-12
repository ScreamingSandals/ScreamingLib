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

package org.screamingsandals.lib.utils.key;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ComparableWrapper;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ResourceLocation implements MappingKey, ComparableWrapper {
    public static final @NotNull Pattern RESOLUTION_PATTERN = Pattern.compile("^(?:(?<namespace>[A-Za-z0-9_.\\-]+):)?(?<key>[A-Za-z0-9_.\\-/ ]+)$");
    public static final @NotNull Pattern VALID_NAMESPACE = Pattern.compile("^[a-z0-9_.\\-]+$");
    public static final @NotNull Pattern VALID_KEY = Pattern.compile("^[a-z0-9_.\\-/]+$");

    private final @NotNull String namespace;
    private final @NotNull String key;

    public static @NotNull Optional<ResourceLocation> ofOptional(@NotNull String combinedString) {
        var matcher = RESOLUTION_PATTERN.matcher(combinedString);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        var namespace = matcher.group("namespace") != null ? matcher.group("namespace").toLowerCase(Locale.ROOT) : "minecraft";
        var key = matcher.group("key").replaceAll(" ", "_").toLowerCase(Locale.ROOT);

        return ofOptional(namespace, key);
    }

    public static @NotNull ResourceLocation of(@NotNull String combinedString) {
        return ofOptional(combinedString).orElseThrow(() -> new IllegalArgumentException(combinedString + " doesn't match validation patterns!"));
    }

    public static @NotNull Optional<ResourceLocation> ofOptional(@NotNull String namespace, @NotNull String key) {
        if (!VALID_NAMESPACE.matcher(namespace).matches() || !VALID_KEY.matcher(key).matches()) {
            return Optional.empty();
        }

        return Optional.of(new ResourceLocation(namespace, key));
    }

    public static @NotNull ResourceLocation of(@NotNull String namespace, @NotNull String key) {
        return ofOptional(namespace, key).orElseThrow(() -> new IllegalArgumentException(namespace + ":" + key + " doesn't match validation patterns!"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof ResourceLocation) {
            return this.namespace.equals(((ResourceLocation) object).namespace) && this.key.equals(((ResourceLocation) object).key);
        }

        var namespacedKey = ResourceLocation.of(object.toString());

        return this.namespace.equals(namespacedKey.namespace) && this.key.equals(namespacedKey.key);
    }

    @Override
    public @NotNull String toString() {
        return namespace + ":" + key;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type.isInstance(this)) {
            return (T) this;
        }
        if (type.isAssignableFrom(String.class)) {
            return (T) type.toString();
        }
        throw new UnsupportedOperationException("Can't convert wrapper to " + type.getName());
    }

    public @NotNull String path() {
        return key;
    }

    public @NotNull String asString() {
        return toString();
    }

    public @NotNull String namespace() {
        return namespace;
    }

    @Override
    public boolean is(@Nullable Object object) {
        return equals(object);
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
