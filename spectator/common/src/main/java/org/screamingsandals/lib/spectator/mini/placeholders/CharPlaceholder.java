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
import lombok.Getter;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

@Data
public abstract class CharPlaceholder implements StringLikePlaceholder {
    @Pattern("[a-z\\d_-]+")
    private final @NotNull String name;

    public abstract char getValue();

    @Override
    public @NotNull String getStringResult(@NotNull List<@NotNull String> arguments, @NotNull Placeholder @NotNull ... placeholders) {
        return String.valueOf(getValue());
    }

    public static final class Constant extends CharPlaceholder {
        @Getter
        private final char value;

        public Constant(@Pattern("[a-z\\d_-]+") @NotNull String name, char value) {
            super(name);
            this.value = value;
        }
    }

    public static final class Lazy extends CharPlaceholder {
        @Getter
        private final @NotNull Supplier<@NotNull Character> supplier;

        public Lazy(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull Character> supplier) {
            super(name);
            this.supplier = supplier;
        }

        @Override
        public char getValue() {
            return supplier.get();
        }
    }
}
