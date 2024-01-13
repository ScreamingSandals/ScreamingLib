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

package org.screamingsandals.lib.impl.utils.visual;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.utils.visual.TextEntry;

@AllArgsConstructor(staticName = "of")
@Data
public final class SimpleCLTextEntry implements TextEntry {
    private final @NotNull String identifier;
    private final @NotNull ComponentLike text;

    public static @NotNull SimpleCLTextEntry of(@NotNull ComponentLike text) {
        return SimpleCLTextEntry.of("", text);
    }

    public @NotNull Component getText() {
        return text.asComponent();
    }

    public @NotNull ComponentLike getComponentLike() {
        return text;
    }
}
