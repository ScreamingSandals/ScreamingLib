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

package org.screamingsandals.lib.lang;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
public final class Translation implements Messageable {
    private final @NotNull List<@NotNull String> keys = new LinkedList<>();
    private final @NotNull ComponentLike fallback;

    private Translation(@NotNull Collection<@NotNull String> keys, @NotNull ComponentLike fallback) {
        this.keys.addAll(keys);
        this.fallback = fallback;
    }

    public static @NotNull Translation of(@NotNull String @NotNull... keys) {
        return of(Arrays.asList(keys), Component.text().content(String.join(".", keys)).color(Color.RED).build());
    }

    public static @NotNull Translation of(@NotNull Collection<@NotNull String> keys) {
        return of(keys, Component.text().content(String.join(".", keys)).color(Color.RED).build());
    }

    public static @NotNull Translation of(@NotNull Collection<@NotNull String> keys, @NotNull ComponentLike fallback) {
        return new Translation(keys, fallback);
    }

    public static @NotNull Translation of(@NotNull ComponentLike fallback, @NotNull String @NotNull... keys) {
        return of(Arrays.asList(keys), fallback);
    }

    public @NotNull Translation join(@NotNull String @NotNull... key) {
        final var copied = new LinkedList<>(keys);
        copied.addAll(Arrays.asList(key));

        return of(copied);
    }

    public @NotNull Translation join(@NotNull Collection<@NotNull String> keys) {
        final var copied = new LinkedList<>(this.keys);
        copied.addAll(keys);

        return of(copied, Component.text().content(String.join(".", keys)).color(Color.RED).build());
    }

    public @NotNull Translation join(@NotNull Collection<@NotNull String> keys, @NotNull Component fallback) {
        final var copied = new LinkedList<>(this.keys);
        copied.addAll(keys);

        return of(copied, fallback);
    }

    public @NotNull Translation join(@NotNull Collection<@NotNull String> keys, @NotNull ComponentLike fallback) {
        final var copied = new LinkedList<>(this.keys);
        copied.addAll(keys);

        return of(copied, fallback);
    }

    @Override
    public boolean needsTranslation() {
        return true;
    }

    @Override
    public @NotNull Type getType() {
        return Type.ADVENTURE;
    }

    public @NotNull Component getFallback() {
        return fallback.asComponent();
    }
}
