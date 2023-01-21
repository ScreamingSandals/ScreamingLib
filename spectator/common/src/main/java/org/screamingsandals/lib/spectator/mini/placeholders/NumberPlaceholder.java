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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@Data
public class NumberPlaceholder implements Placeholder {
    @Pattern("[a-z\\d_-]+")
    private final @NotNull String name;
    private final @NotNull Number value;

    @SuppressWarnings("unchecked")
    @Override
    public <B extends Component.Builder<B, C>, C extends Component> @NotNull B getResult(@NotNull MiniMessageParser parser, @NotNull List<@NotNull String> arguments, @NotNull Placeholder @NotNull... placeholders) {
        if (arguments.size() == 1) {
            try {
                // locale
                var locale = Locale.forLanguageTag(arguments.get(0));
                var format = DecimalFormat.getInstance(locale);
                return (B) Component.text().content(format.format(value));
            } catch (Throwable ignored) {
                // format
                try {
                    var format = new DecimalFormat(arguments.get(0), DecimalFormatSymbols.getInstance());
                    return parser.parseIntoBuilder(format.format(value), placeholders);
                } catch (Throwable ignored2) {}
            }
        } else if (arguments.size() == 2) {
            try {
                // locale & format
                var locale = Locale.forLanguageTag(arguments.get(0));
                var format = new DecimalFormat(arguments.get(1), new DecimalFormatSymbols(locale));
                return parser.parseIntoBuilder(format.format(value), placeholders);
            } catch (Throwable ignored) {
            }
        }

        return (B) Component.text().content(value);
    }
}
