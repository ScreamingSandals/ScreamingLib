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
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;

import java.util.List;
import java.util.function.BooleanSupplier;

@Data
public class LazyBooleanPlaceholder implements Placeholder {
    @Pattern("[a-z\\d_-]+")
    private final String name;
    private final BooleanSupplier supplier;

    // addition: custom strings for true/false
    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public <B extends Component.Builder<B, C>, C extends Component> B getResult(MiniMessageParser parser, List<String> arguments, Placeholder... placeholders) {
        var value = supplier.getAsBoolean();

        if (arguments.size() == 2) {
            if (value) {
                return parser.parseIntoBuilder(arguments.get(0), placeholders);
            } else {
                return parser.parseIntoBuilder(arguments.get(1), placeholders);
            }
        }

        return (B) Component.text().content(value);
    }
}
