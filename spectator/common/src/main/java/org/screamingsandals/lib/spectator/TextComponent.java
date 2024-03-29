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

package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.spectator.Spectator;

public interface TextComponent extends Component {
    @Contract(value = "-> new", pure = true)
    static TextComponent.@NotNull Builder builder() {
        return Spectator.getBackend().text();
    }

    @NotNull String content();

    @Contract(pure = true)
    @NotNull TextComponent withContent(@NotNull String content);

    @Contract(value = "-> new", pure = true)
    TextComponent.@NotNull Builder toBuilder();

    interface Builder extends Component.Builder<Builder, TextComponent> {
        @Contract("_ -> this")
        @NotNull Builder content(@NotNull String content);

        @Contract("_ -> this")
        default @NotNull Builder content(byte value) {
            return content(String.valueOf(value));
        }

        @Contract("_ -> this")
        default @NotNull Builder content(short value) {
            return content(String.valueOf(value));
        }

        @Contract("_ -> this")
        default @NotNull Builder content(int value) {
            return content(String.valueOf(value));
        }

        @Contract("_ -> this")
        default @NotNull Builder content(long value) {
            return content(String.valueOf(value));
        }

        @Contract("_ -> this")
        default @NotNull Builder content(float value) {
            return content(String.valueOf(value));
        }

        @Contract("_ -> this")
        default @NotNull Builder content(double value) {
            return content(String.valueOf(value));
        }

        @Contract("_ -> this")
        default @NotNull Builder content(@NotNull Number value) {
            return content(String.valueOf(value));
        }

        @Contract("_ -> this")
        default @NotNull Builder content(boolean value) {
            return content(String.valueOf(value));
        }

        @Contract("_ -> this")
        default @NotNull Builder content(char value) {
            return content(String.valueOf(value));
        }
    }
}
