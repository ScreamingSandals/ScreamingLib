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

package org.screamingsandals.lib.adventure.spectator;

import net.kyori.adventure.text.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SpectatorBackend;

public class AdventureBackend implements SpectatorBackend {
    @Override
    public Component empty() {
        return new AdventureComponent(net.kyori.adventure.text.Component.empty());
    }

    @Nullable
    @Contract("null -> null; !null -> !null")
    public static Component wrapComponent(@Nullable net.kyori.adventure.text.Component component) {
        if (component == null) {
            return null;
        }

        if (component instanceof StorageNBTComponent) {
            return new AdventureStorageNBTComponent((StorageNBTComponent) component);
        }

        if (component instanceof EntityNBTComponent) {
            return new AdventureEntityNBTComponent((EntityNBTComponent) component);
        }

        if (component instanceof BlockNBTComponent) {
            return new AdventureBlockNBTComponent((BlockNBTComponent) component);
        }

        if (component instanceof TranslatableComponent) {
            return new AdventureTranslatableComponent((TranslatableComponent) component);
        }

        if (component instanceof SelectorComponent) {
            return new AdventureSelectorComponent((SelectorComponent) component);
        }

        if (component instanceof ScoreComponent) {
            return new AdventureScoreComponent((ScoreComponent) component);
        }

        if (component instanceof KeybindComponent) {
            return new AdventureKeybindComponent((KeybindComponent) component);
        }

        if (component instanceof TextComponent) {
            return new AdventureTextComponent((TextComponent) component);
        }

        return new AdventureComponent(component);
    }
}
