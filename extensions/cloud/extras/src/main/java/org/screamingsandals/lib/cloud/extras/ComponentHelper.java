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

//
// MIT License
//
// Copyright (c) 2021 Alexander Söderberg & Contributors
// Copyright (c) 2022 ScreamingSandals
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
package org.screamingsandals.lib.cloud.extras;

import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.TextComponent;

import static org.screamingsandals.lib.spectator.Component.text;

@UtilityClass
public final class ComponentHelper {

    public static final @NotNull Pattern SPECIAL_CHARACTERS_PATTERN = Pattern.compile("[^\\s\\w\\-]");

    public static @NotNull Component highlight(final @NotNull Component component, final @NotNull Color highlightColor) {
        return component; // TODO
        /*return component.replaceText(config -> {
            config.match(SPECIAL_CHARACTERS_PATTERN);
            config.replacement(match -> match.color(highlightColor));
        });*/
    }

    public static int length(final @NotNull Component component) {
        int length = 0;
        if (component instanceof TextComponent) {
            length += ((TextComponent) component).content().length();
        }
        /*final Component translated = GlobalTranslator.render(component, Locale.getDefault());*/
        for (final var child : component.children()) {
            length += length(child);
        }
        return length;
    }

    public static @NotNull Component line(int length, @NotNull Color color) {
        return text().content("-").color(color).strikethrough().build().repeat(length);
    }

    public static @NotNull Component branch(@NotNull Color color) {
        return text("├─", color);
    }

    public static @NotNull Component lastBranch(@NotNull Color color) {
        return text("└─", color);
    }

    public static @NotNull Component header(@NotNull Component title, int headerFooterLength, @NotNull Color color) {
        final int sideLength = (headerFooterLength - ComponentHelper.length(title)) / 2;
        return text()
                .append(line(sideLength, color))
                .append(title)
                .append(line(sideLength, color))
                .build();
    }

}
