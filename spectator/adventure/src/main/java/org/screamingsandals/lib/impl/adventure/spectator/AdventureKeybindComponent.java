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

package org.screamingsandals.lib.impl.adventure.spectator;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.KeybindComponent;

public class AdventureKeybindComponent extends AdventureComponent implements KeybindComponent {
    public AdventureKeybindComponent(net.kyori.adventure.text.@NotNull KeybindComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String keybind() {
        return ((net.kyori.adventure.text.KeybindComponent) wrappedObject).keybind();
    }

    @Override
    public @NotNull KeybindComponent withKeybind(@NotNull String keybind) {
        return (KeybindComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.KeybindComponent) wrappedObject).keybind(keybind));
    }

    @Override
    public KeybindComponent.@NotNull Builder toBuilder() {
        return new AdventureKeybindBuilder(((net.kyori.adventure.text.KeybindComponent) wrappedObject).toBuilder());
    }

    public static class AdventureKeybindBuilder extends AdventureBuilder<
            net.kyori.adventure.text.KeybindComponent,
            KeybindComponent.Builder,
            KeybindComponent,
            net.kyori.adventure.text.KeybindComponent.Builder
            > implements KeybindComponent.Builder {

        public AdventureKeybindBuilder(net.kyori.adventure.text.KeybindComponent.Builder builder) {
            super(builder);
        }

        @Override
        public KeybindComponent.@NotNull Builder keybind(@NotNull String keybind) {
            getBuilder().keybind(keybind);
            return self();
        }
    }
}
