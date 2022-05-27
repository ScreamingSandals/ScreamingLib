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

package org.screamingsandals.lib.spectator.mini;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.minitag.MiniTagParser;
import org.screamingsandals.lib.minitag.tags.TagType;
import org.screamingsandals.lib.minitag.tags.TransformedTag;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.mini.resolvers.ComponentBuilderResolver;
import org.screamingsandals.lib.spectator.mini.resolvers.StylingResolver;
import org.screamingsandals.lib.spectator.mini.transformers.NegatedDecorationTransformer;
import org.screamingsandals.lib.spectator.mini.transformers.TagToAttributeTransformer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Data
public class MiniMessageParser {
    private final MiniTagParser parser;
    private final Map<String, ComponentBuilderResolver> componentTagResolvers = new HashMap<>();
    private final Map<String, StylingResolver> componentStylingResolvers = new HashMap<>();

    public MiniMessageParser(@NotNull MiniTagParser.Builder builder) {
        var colorTransformedTag = new TransformedTag(TagType.PAIR, new TagToAttributeTransformer("color"));
        var negatedDecorationTag = new TransformedTag(TagType.PAIR, new NegatedDecorationTransformer());

        this.parser = builder
                .resetTag(true)

                .registerTag("color", TagType.PAIR, "colour", "c")
                .registerTag("black", colorTransformedTag)
                .registerTag("dark_blue", colorTransformedTag)
                .registerTag("dark_green", colorTransformedTag)
                .registerTag("dark_aqua", colorTransformedTag)
                .registerTag("dark_red", colorTransformedTag)
                .registerTag("dark_purple", colorTransformedTag)
                .registerTag("gold", colorTransformedTag)
                .registerTag("gray", colorTransformedTag, "grey")
                .registerTag("dark_gray", colorTransformedTag, "dark_grey")
                .registerTag("blue", colorTransformedTag)
                .registerTag("green", colorTransformedTag)
                .registerTag("aqua", colorTransformedTag)
                .registerTag("red", colorTransformedTag)
                .registerTag("light_purple", colorTransformedTag)
                .registerTag("yellow", colorTransformedTag)
                .registerTag("white", colorTransformedTag)
                .registerTag(Pattern.compile("#([\\dA-Fa-f]{6}|[\\dA-Fa-f]{3})"), colorTransformedTag)

                .registerTag("bold", TagType.PAIR, "b")
                .registerTag("italic", TagType.PAIR, "i", "em")
                .registerTag("underlined", TagType.PAIR, "u")
                .registerTag("strikethrough", TagType.PAIR, "st")
                .registerTag("obfuscated", TagType.PAIR, "obf")

                .registerTag("!bold", negatedDecorationTag, "!b")
                .registerTag("!italic", negatedDecorationTag, "!i", "!em")
                .registerTag("!underlined", negatedDecorationTag, "!u")
                .registerTag("!strikethrough", negatedDecorationTag, "!st")
                .registerTag("!obfuscated", negatedDecorationTag, "!obf")

                .registerTag("click", TagType.PAIR)
                .registerTag("hover", TagType.PAIR)
                .registerTag("key", TagType.PAIR)
                .registerTag("lang", TagType.PAIR, "tr", "translate")
                .registerTag("insertion", TagType.PAIR)
                //.registerTag("rainbow", TagType.PAIR)
                //.registerTag("gradient", TagType.PAIR)
                //.registerTag("transition", TagType.PAIR)
                .registerTag("font", TagType.PAIR)
                .registerTag("newline", TagType.SINGLE, "br")

                .build();
    }

    public MiniMessageParser() {
        this(MiniTagParser.builder());
    }
    @NotNull
    public Component parse(@NotNull String str, @NotNull Placeholder... placeholders) {
        if (str.isEmpty()) {
            return Component.empty();
        }

        var resolved = parser.parse(str);

        if (!resolved.hasChildren()) {
            return Component.empty();
        }


        return null;
    }
}
