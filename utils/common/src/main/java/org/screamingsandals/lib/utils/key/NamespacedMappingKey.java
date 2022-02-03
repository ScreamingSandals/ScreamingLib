/*
 * Copyright 2022 ScreamingSandals
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
import org.screamingsandals.lib.utils.ComparableWrapper;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NamespacedMappingKey implements MappingKey, ComparableWrapper {
    public static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(?:(?<namespace>[A-Za-z0-9_.\\-]+):)?(?<key>[A-Za-z0-9_.\\-/ ]+)$");
    public static final Pattern VALID_NAMESPACE = Pattern.compile("^[a-z0-9_.\\-]+$");
    public static final Pattern VALID_KEY = Pattern.compile("^[a-z0-9_.\\-/]+$");

    private final String namespace;
    private final String key;

    public static Optional<NamespacedMappingKey> ofOptional(String combinedString) {
        var matcher = RESOLUTION_PATTERN.matcher(combinedString);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        var namespace = matcher.group("namespace") != null ? matcher.group("namespace").toLowerCase() : "minecraft";
        var key = matcher.group("key").replaceAll(" ", "_").toLowerCase();

        return ofOptional(namespace, key);
    }

    public static NamespacedMappingKey of(String combinedString) {
        return ofOptional(combinedString).orElseThrow(() -> new IllegalArgumentException(combinedString + " doesn't match validation patterns!"));
    }

    public static Optional<NamespacedMappingKey> ofOptional(String namespace, String key) {
        if (!VALID_NAMESPACE.matcher(namespace).matches() || !VALID_KEY.matcher(key).matches()) {
            return Optional.empty();
        }

        return Optional.of(new NamespacedMappingKey(namespace, key));
    }

    public static NamespacedMappingKey of(String namespace, String key) {
        return ofOptional(namespace, key).orElseThrow(() -> new IllegalArgumentException(namespace + ":" + key + " doesn't match validation patterns!"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof NamespacedMappingKey) {
            return this.namespace.equals(((NamespacedMappingKey) object).namespace) && this.key.equals(((NamespacedMappingKey) object).key);
        }

        var namespacedKey = NamespacedMappingKey.of(object.toString());

        return this.namespace.equals(namespacedKey.namespace) && this.key.equals(namespacedKey.key);
    }

    @Override
    @NotNull
    public String toString() {
        return namespace + ":" + key;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(this)) {
            return (T) this;
        }
        if (type.isAssignableFrom(String.class)) {
            return (T) type.toString();
        }
        throw new UnsupportedOperationException("Can't convert wrapper to " + type.getName());
    }

    @NotNull
    public String value() {
        return key;
    }

    @NotNull
    public String asString() {
        return toString();
    }

    @NotNull
    public String namespace() {
        return namespace;
    }

    @Override
    public boolean is(Object object) {
        return equals(object);
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
