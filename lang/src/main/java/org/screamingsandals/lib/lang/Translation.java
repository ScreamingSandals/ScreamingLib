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

package org.screamingsandals.lib.lang;

import lombok.Data;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
public final class Translation implements Messageable {
    private final List<String> keys = new LinkedList<>();
    private final Component fallback;

    private Translation(Collection<String> keys, Component fallback) {
        this.keys.addAll(keys);
        this.fallback = fallback;
    }

    public static Translation of(String... keys) {
        return of(Arrays.asList(keys), Component.text().content(String.join(".", keys)).color(Color.RED).build());
    }

    public static Translation of(Collection<String> keys) {
        return of(keys, Component.text().content(String.join(".", keys)).color(Color.RED).build());
    }

    public static Translation of(Collection<String> keys, Component fallback) {
        return new Translation(keys, fallback);
    }

    public static Translation of(Collection<String> keys, ComponentLike fallback) {
        return new Translation(keys, fallback.asComponent());
    }

    public Translation join(String... key) {
        final var copied = new LinkedList<>(keys);
        copied.addAll(Arrays.asList(key));

        return of(copied);
    }

    public Translation join(Collection<String> keys) {
        final var copied = new LinkedList<>(this.keys);
        copied.addAll(keys);

        return of(copied, Component.text().content(String.join(".", keys)).color(Color.RED).build());
    }

    public Translation join(Collection<String> keys, Component fallback) {
        final var copied = new LinkedList<>(this.keys);
        copied.addAll(keys);

        return of(copied, fallback);
    }

    public Translation join(Collection<String> keys, ComponentLike fallback) {
        final var copied = new LinkedList<>(this.keys);
        copied.addAll(keys);

        return of(copied, fallback);
    }

    @Override
    public boolean needsTranslation() {
        return true;
    }

    @Override
    public Type getType() {
        return Type.ADVENTURE;
    }
}
