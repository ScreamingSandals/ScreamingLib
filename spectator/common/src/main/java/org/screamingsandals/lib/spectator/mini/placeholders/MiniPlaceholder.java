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
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;

import java.util.List;
import java.util.function.Supplier;

@Data
public abstract class MiniPlaceholder implements Placeholder {
    @Pattern("[a-z\\d_-]+")
    private final @NotNull String name;

    public abstract @NotNull String getValue();

    @Override
    public <B extends Component.Builder<B, C>, C extends Component> @NotNull B getResult(@NotNull MiniMessageParser parser, @NotNull List<@NotNull String> arguments, @NotNull Placeholder @NotNull... placeholders) {
        return parser.parseIntoBuilder(getValue(), placeholders);
    }

    public static final class Constant extends MiniPlaceholder {
        @Getter
        private final @NotNull String value;

        public Constant(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull String value) {
            super(name);
            this.value = value;
        }
    }

    public static final class Lazy extends MiniPlaceholder {
        @Getter
        private final @NotNull Supplier<@NotNull String> supplier;

        public Lazy(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull String> supplier) {
            super(name);
            this.supplier = supplier;
        }

        @Override
        public @NotNull String getValue() {
            return supplier.get();
        }
    }
}
