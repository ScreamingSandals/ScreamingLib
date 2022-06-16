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

package org.screamingsandals.lib.spectator.mini.placeholders;

import lombok.Data;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.function.Supplier;

@Data
public class LazyDateTimePlaceholder implements Placeholder {
    @Pattern("[a-z\\d_-]+")
    private final String name;
    private final Supplier<TemporalAccessor> supplier;

    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public <B extends Component.Builder<B, C>, C extends Component> B getResult(MiniMessageParser parser, List<String> arguments, Placeholder... placeholders) {
        var value = supplier.get();

        if (arguments.size() >= 1) {
            try {
                return (B) Component.text().content(DateTimeFormatter.ofPattern(arguments.get(0)).format(value));
            } catch (Throwable ignored) {
            }
        }

        return (B) Component.text().content(DateTimeFormatter.ISO_DATE_TIME.format(value));
    }
}
