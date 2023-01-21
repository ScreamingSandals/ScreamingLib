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

package org.screamingsandals.lib.spectator.mini.placeholders;

import lombok.Data;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.*;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;

import java.util.List;

@Data
public class ComponentPlaceholder implements Placeholder {
    @Pattern("[a-z\\d_-]+")
    private final @NotNull String name;
    private final @NotNull Component value;

    @SuppressWarnings("unchecked")
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> @NotNull B getResult(@NotNull MiniMessageParser parser, @NotNull List<@NotNull String> arguments, @NotNull Placeholder @NotNull... placeholders) {
        if (value instanceof TextComponent) {
            return (B) ((TextComponent) value).toBuilder();
        } else if (value instanceof TranslatableComponent) {
            return (B) ((TranslatableComponent) value).toBuilder();
        } else if (value instanceof BlockNBTComponent) {
            return (B) ((BlockNBTComponent) value).toBuilder();
        } else if (value instanceof EntityNBTComponent) {
            return (B) ((EntityNBTComponent) value).toBuilder();
        } else if (value instanceof KeybindComponent) {
            return (B) ((KeybindComponent) value).toBuilder();
        } else if (value instanceof ScoreComponent) {
            return (B) ((ScoreComponent) value).toBuilder();
        } else if (value instanceof SelectorComponent) {
            return (B) ((SelectorComponent) value).toBuilder();
        } else if (value instanceof StorageNBTComponent) {
            return (B) ((StorageNBTComponent) value).toBuilder();
        }

        // How did we get here?
        return (B) Component.text().append(value);
    }
}
