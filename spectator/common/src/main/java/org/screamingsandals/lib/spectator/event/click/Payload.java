/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.spectator.event.click;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.nbt.Tag;
import org.screamingsandals.lib.spectator.dialog.Dialog;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

public interface Payload {
    @Contract(value = "_ -> new", pure = true)
    static Payload.@NotNull Text text(@NotNull String text) {
        return new Payload.Text.Default(text);
    }

    @Contract(value = "_ -> new", pure = true)
    static Payload.@NotNull Int integer(int number) {
        return new Payload.Int.Default(number);
    }

    @LimitedVersionSupport(">= 1.21.6")
    @Contract(value = "_, _ -> new", pure = true)
    static Payload.@NotNull Custom custom(@NotNull ResourceLocation location, @NotNull Tag tag) {
        return new Payload.Custom.Default(location, tag);
    }

    @LimitedVersionSupport(">= 1.21.6")
    @Contract(value = "_ -> new", pure = true)
    static @NotNull ShowDialog showDialog(@NotNull Dialog dialog) {
        return new ShowDialog.Default(dialog);
    }

    interface Text extends Payload {
        @NotNull String text();

        @Data
        @Accessors(fluent = true)
        final class Default implements Text {
            private final @NotNull String text;
        }
    }

    interface Int extends Payload {
        int number();

        @Data
        @Accessors(fluent = true)
        final class Default implements Int {
            private final int number;
        }
    }

    @LimitedVersionSupport(">= 1.21.6")
    interface Custom extends Payload {
        @NotNull ResourceLocation location();

        @NotNull Tag tag();

        @Data
        @Accessors(fluent = true)
        final class Default implements Custom {
            private final @NotNull ResourceLocation location;
            private final @NotNull Tag tag;
        }
    }

    @LimitedVersionSupport(">= 1.21.6")
    interface ShowDialog extends Payload {
        @NotNull Dialog dialog();

        @Data
        @Accessors(fluent = true)
        final class Default implements ShowDialog {
            private final @NotNull Dialog dialog;
        }
    }
}
