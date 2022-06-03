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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.minitag.MiniTagParser;
import org.screamingsandals.lib.minitag.nodes.Node;
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.minitag.nodes.TextNode;
import org.screamingsandals.lib.minitag.tags.TagType;
import org.screamingsandals.lib.minitag.tags.TransformedTag;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;
import org.screamingsandals.lib.spectator.mini.resolvers.*;
import org.screamingsandals.lib.spectator.mini.transformers.NegatedDecorationTransformer;
import org.screamingsandals.lib.spectator.mini.transformers.TagToAttributeTransformer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class MiniMessageParser {

    public static final MiniMessageParser INSTANCE = new MiniMessageParser();
    private final MiniTagParser parser;
    private final Map<String, ComponentBuilderResolver> componentTagResolvers = new HashMap<>();
    private final Map<String, StylingResolver> componentStylingResolvers = new HashMap<>();

    public MiniMessageParser(@NotNull MiniTagParser.Builder builder) {
        // TODO: some builder, this is fucking mess
        var colorTransformedTag = new TransformedTag(TagType.PAIR, new TagToAttributeTransformer("color"));
        var negatedDecorationTag = new TransformedTag(TagType.PAIR, new NegatedDecorationTransformer());

        componentTagResolvers.put("selector", new SelectorResolver());
        componentTagResolvers.put("lang", new TranslatableResolver());
        componentTagResolvers.put("key", new KeybindResolver());

        componentStylingResolvers.put("bold", new BoldResolver());
        componentStylingResolvers.put("click", new ClickResolver());
        componentStylingResolvers.put("color", new ColorResolver());
        componentStylingResolvers.put("font", new FontResolver());
        componentStylingResolvers.put("hover", new HoverResolver());
        componentStylingResolvers.put("insertion", new InsertionResolver());
        componentStylingResolvers.put("italic", new ItalicResolver());
        componentStylingResolvers.put("obfuscated", new ObfuscatedResolver());
        componentStylingResolvers.put("strikethrough", new StrikethroughResolver());
        componentStylingResolvers.put("underlined", new UnderlinedResolver());

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
                .registerTag("key", TagType.SINGLE)
                .registerTag("lang", TagType.SINGLE, "tr", "translate")
                .registerTag("selector", TagType.SINGLE, "sel")
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

        if (resolved.children().size() == 1) {
            return resolveChildren(resolved.children().get(0), placeholders).build();
        } else {
            return Component.text()
                    .append(resolved.children().stream().map(node -> resolveChildren(node, placeholders).build())
                    .collect(Collectors.toList())).build();
        }
    }
    @SuppressWarnings("unchecked")
    @NotNull
    @ApiStatus.Internal
    public <B extends Component.Builder<B, C>, C extends Component> B parseIntoBuilder(@NotNull String str, @NotNull Placeholder... placeholders) {
        if (str.isEmpty()) {
            return (B) Component.text();
        }

        var resolved = parser.parse(str);

        if (!resolved.hasChildren()) {
            return (B) Component.text();
        }

        if (resolved.children().size() == 1) {
            return resolveChildren(resolved.children().get(0), placeholders);
        } else {
            return (B) Component.text()
                    .append(resolved.children().stream().map(node -> resolveChildren(node, placeholders).build())
                            .collect(Collectors.toList()));
        }
    }

    @SuppressWarnings("unchecked")
    private <B extends Component.Builder<B, C>, C extends Component> B resolveChildren(Node node, @NotNull Placeholder... placeholders) {
        if (node instanceof TextNode) {
            return (B) Component.text().content(((TextNode) node).getText());
        } else if (node instanceof TagNode) {
            if (componentTagResolvers.containsKey(((TagNode) node).getTag().toLowerCase())) {
                if (node.hasChildren()) {
                    throw new IllegalArgumentException("Tags defining non-text components can't be pair tags!");
                }
                return componentTagResolvers.get(((TagNode) node).getTag().toLowerCase()).resolve(this, (TagNode) node, placeholders);
            } else if (componentStylingResolvers.containsKey(((TagNode) node).getTag().toLowerCase())) {
                var res = componentStylingResolvers.get(((TagNode) node).getTag().toLowerCase());

                if (node.hasChildren()) {
                    var resolved = node.children();
                    if (resolved.size() == 1) {
                        var child = (B) resolveChildren(resolved.get(0), placeholders);
                        res.resolve(this, child, (TagNode) node, placeholders);
                        return child;
                    } else {
                        var component = (B) Component.text();
                        res.resolve(this, component, (TagNode) node, placeholders);
                        return component
                                .append(resolved.stream().map(n -> resolveChildren(n, placeholders).build())
                                        .collect(Collectors.toList()));
                    }
                } else {
                    var component = (B) Component.text();
                    res.resolve(this, component, (TagNode) node);
                    return component;
                }
            } else if (node.hasChildren()) {
                // wtf?
                var resolved = node.children();
                if (resolved.size() == 1) {
                    return resolveChildren(resolved.get(0), placeholders);
                } else {
                    return (B) Component.text()
                            .append(resolved.stream().map(n -> resolveChildren(n, placeholders).build())
                                    .collect(Collectors.toList()));
                }
            } else {
                for (var p : placeholders) {
                    if (p.getName().equals(((TagNode) node).getTag())) {
                        return p.getResult(this, ((TagNode) node).getArgs(), placeholders);
                    }
                }
                return (B) Component.text()
                        .content(parser.tagOpeningSymbol()
                                + ((TagNode) node).getTag()
                                + (!((TagNode) node).getArgs().isEmpty() ? ":" : "")
                                + String.join(":", ((TagNode) node).getArgs())
                                + parser.tagClosingSymbol()
                        );
            }
        } else {
            throw new IllegalArgumentException("Unknown node type!");
        }
    }
}
