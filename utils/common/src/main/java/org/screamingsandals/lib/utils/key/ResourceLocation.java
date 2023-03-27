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
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ComparableWrapper;

import java.util.Arrays;
import java.util.Objects;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class ResourceLocation implements MappingKey, ComparableWrapper {
    private final @NotNull String namespace;
    private final @NotNull String path;

    public static @Nullable ResourceLocation ofNullable(@Nullable String combinedString) {
        if (combinedString == null) {
            return null;
        }

        @Nullable String namespace = null;
        var builder = new StringBuilder(combinedString.length());
        var length = combinedString.length();
        var whitespaceCharacter = false;
        var slashUsed = false;
        for (var i = 0; i < length; i++) {
            var c = combinedString.charAt(i);
            if (c == ' ') { // we treat whitespaces as underscores
                whitespaceCharacter = true;
                continue;
            } else if (whitespaceCharacter) {
                whitespaceCharacter = false;
                if (c != '/' && c != ':' && builder.length() != 0) {
                    builder.append('_');
                }
            }

            if (c >= 'A' && c <= 'Z') { // we accept these characters, however we lowercase them
                builder.append((char) (c + 0x20)); // lowercase these characters
            } else if (c == '/') {
                // valid character but can't be used in namespace, only in path
                slashUsed = true;
                builder.append('/');
            } else if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.' || c == '_' || c == '-') {
                builder.append(c);
            } else if (c == ':' && namespace == null) {
                if (slashUsed) {
                    return null;
                }
                namespace = builder.toString();
                builder.setLength(0);
            } else {
                return null;
            }
        }
        if (builder.length() == 0) {
            return null;
        }
        return new ResourceLocation(namespace == null ? "minecraft" : namespace, builder.toString());
    }

    public static @NotNull ResourceLocation of(@NotNull String combinedString) {
        var result = ofNullable(combinedString);
        if (result == null) {
            throw new IllegalArgumentException(combinedString + " doesn't match validation patterns!");
        }
        return result;
    }

    public static @Nullable ResourceLocation ofNullable(@NotNull String namespace, @NotNull String path) {
        var length = namespace.length();
        for (var i = 0; i < length; i++) {
            var c = namespace.charAt(i);
            if ((c < 'a' || c > 'z') && (c < '0' || c > '9') && c != '.' && c != '_' && c != '-') {
                return null;
            }
        }
        length = path.length();
        for (var i = 0; i < length; i++) {
            var c = path.charAt(i);
            if ((c < 'a' || c > 'z') && (c < '0' || c > '9') && c != '.' && c != '_' && c != '-' && c != '/') {
                return null;
            }
        }

        return new ResourceLocation(namespace, path);
    }

    public static @NotNull ResourceLocation of(@NotNull String namespace, @NotNull String path) {
        var result = ofNullable(namespace, path);
        if (result == null) {
            throw new IllegalArgumentException(namespace + ":" + path + " doesn't match validation patterns!");
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof ResourceLocation) {
            return this.namespace.equals(((ResourceLocation) object).namespace) && this.path.equals(((ResourceLocation) object).path);
        }

        var namespacedKey = ResourceLocation.of(object.toString());

        return this.namespace.equals(namespacedKey.namespace) && this.path.equals(namespacedKey.path);
    }

    @Override
    public @NotNull String toString() {
        return namespace + ":" + path;
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

    public @NotNull String asString() {
        return toString();
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
