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

package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

@LimitedVersionSupport(">= 1.12")
public interface KeybindComponent extends Component {
    @Contract(value = "-> new", pure = true)
    @NotNull
    static KeybindComponent.Builder builder() {
        return Spectator.getBackend().keybind();
    }

    @NotNull
    String keybind();

    @Contract(pure = true)
    @NotNull
    KeybindComponent withKeybind(@NotNull String keybind);

    @Contract(value = "-> new", pure = true)
    @NotNull
    KeybindComponent.Builder toBuilder();

    interface Builder extends Component.Builder<Builder, KeybindComponent> {
        @Contract("_ -> this")
        @NotNull
        Builder keybind(@NotNull String keybind);
    }
}
