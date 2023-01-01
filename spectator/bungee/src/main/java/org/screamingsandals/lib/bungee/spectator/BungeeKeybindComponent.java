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

package org.screamingsandals.lib.bungee.spectator;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.KeybindComponent;

public class BungeeKeybindComponent extends BungeeComponent implements KeybindComponent {
    protected BungeeKeybindComponent(net.md_5.bungee.api.chat.@NotNull KeybindComponent wrappedObject) {
        super(wrappedObject);
    }

    @NotNull
    public String keybind() {
        return ((net.md_5.bungee.api.chat.KeybindComponent) wrappedObject).getKeybind();
    }

    @Override
    @NotNull
    public KeybindComponent withKeybind(@NotNull String keybind) {
        var duplicate = (net.md_5.bungee.api.chat.KeybindComponent) wrappedObject.duplicate();
        duplicate.setKeybind(keybind);
        return (KeybindComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @NotNull
    @Override
    public KeybindComponent.Builder toBuilder() {
        var duplicate = (net.md_5.bungee.api.chat.KeybindComponent) wrappedObject.duplicate();
        return new BungeeKeybindBuilder(duplicate);
    }

    public static class BungeeKeybindBuilder extends BungeeBuilder<KeybindComponent, KeybindComponent.Builder, net.md_5.bungee.api.chat.KeybindComponent> implements KeybindComponent.Builder {

        public BungeeKeybindBuilder(net.md_5.bungee.api.chat.KeybindComponent component) {
            super(component);
        }

        @Override
        @NotNull
        public KeybindComponent.Builder keybind(@NotNull String keybind) {
            component.setKeybind(keybind);
            return this;
        }
    }
}
