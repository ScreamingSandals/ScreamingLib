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

package org.screamingsandals.lib.spectator.mini.placeholders;

import lombok.Data;
import lombok.Getter;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;

import java.util.List;
import java.util.function.BooleanSupplier;

@Data
public abstract class BooleanPlaceholder implements Placeholder, StringLikePlaceholder {
    @Pattern("[a-z\\d_-]+")
    private final @NotNull String name;

    public abstract boolean isValue();

    // addition: custom strings for true/false
    @SuppressWarnings("unchecked")
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> @NotNull B getResult(@NotNull MiniMessageParser parser, @NotNull List<@NotNull String> arguments, @NotNull Placeholder @NotNull... placeholders) {
        var value = isValue();

        if (arguments.size() == 2) {
            if (value) {
                return parser.parseIntoBuilder(arguments.get(0), placeholders);
            } else {
                return parser.parseIntoBuilder(arguments.get(1), placeholders);
            }
        }

        return (B) Component.text().content(value);
    }

    @Override
    public @NotNull String getStringResult(@NotNull MiniMessageParser parser, @NotNull List<@NotNull String> arguments, @NotNull Placeholder @NotNull ... placeholders) {
        var value = isValue();

        if (arguments.size() == 2) {
            if (value) {
                return parser.resolvePlaceholdersInString(arguments.get(0), placeholders);
            } else {
                return parser.resolvePlaceholdersInString(arguments.get(1), placeholders);
            }
        }

        return String.valueOf(value);
    }

    public static final class Constant extends BooleanPlaceholder {
        @Getter
        private final boolean value;

        public Constant(@Pattern("[a-z\\d_-]+") @NotNull String name, boolean value) {
            super(name);
            this.value = value;
        }
    }

    public static final class Lazy extends BooleanPlaceholder {
        @Getter
        private final @NotNull BooleanSupplier supplier;

        public Lazy(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull BooleanSupplier supplier) {
            super(name);
            this.supplier = supplier;
        }

        @Override
        public boolean isValue() {
            return supplier.getAsBoolean();
        }
    }
}
