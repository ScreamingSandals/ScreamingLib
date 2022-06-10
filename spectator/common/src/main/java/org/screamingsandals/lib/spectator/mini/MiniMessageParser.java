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

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MiniMessageParser {

    public static final MiniMessageParser INSTANCE = MiniMessageParser.builder()
            .defaultStylingTags()
            .defaultComponentTags()
            .resetTag(true)
            .build();
    private final MiniTagParser parser;
    private final Map<String, ComponentBuilderResolver> componentTagResolvers;
    private final Map<String, StylingResolver> componentStylingResolvers;

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

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final MiniTagParser.Builder miniTagParserBuilder = MiniTagParser.builder();
        private final Map<String, ComponentBuilderResolver> componentTagResolvers = new HashMap<>();
        private final Map<String, StylingResolver> componentStylingResolvers = new HashMap<>();

        @NotNull
        @Contract("_ -> this")
        public Builder strictClosing(boolean strictClosing) {
            miniTagParserBuilder.strictClosing(strictClosing);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder escapeInvalidEndings(boolean escapeInvalidEndings) {
            miniTagParserBuilder.escapeInvalidEndings(escapeInvalidEndings);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder preTag(@Nullable String preTag) {
            miniTagParserBuilder.preTag(preTag);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder preTag(boolean enablePreTag) {
            miniTagParserBuilder.preTag(enablePreTag);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder resetTag(@Nullable String resetTag) {
            miniTagParserBuilder.resetTag(resetTag);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder resetTag(boolean enableResetTag) {
            miniTagParserBuilder.resetTag(enableResetTag);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder escapeSymbol(char escapeSymbol) {
            miniTagParserBuilder.escapeSymbol(escapeSymbol);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder tagOpeningSymbol(char tagOpeningSymbol) {
            miniTagParserBuilder.tagOpeningSymbol(tagOpeningSymbol);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder tagClosingSymbol(char tagClosingSymbol) {
            miniTagParserBuilder.tagClosingSymbol(tagClosingSymbol);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder endingTagSymbol(char endingTagSymbol) {
            miniTagParserBuilder.endingTagSymbol(endingTagSymbol);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder argumentSeparator(char argumentSeparator) {
            miniTagParserBuilder.argumentSeparator(argumentSeparator);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder quotes(List<Character> quotes) {
            miniTagParserBuilder.quotes(quotes);
            return this;
        }

        @NotNull
        @Contract("_ -> this")
        public Builder quotes(Character... quotes) {
            miniTagParserBuilder.quotes(quotes);
            return this;
        }

        @NotNull
        @Contract("_, _, _ -> this")
        public Builder registerStylingTag(String name, StylingResolver resolver, String... aliases) {
            miniTagParserBuilder.registerTag(name, TagType.PAIR, aliases);
            componentStylingResolvers.put(name, resolver);
            return this;
        }

        @NotNull
        @Contract("_, _, _ -> this")
        public Builder registerComponentTag(String name, ComponentBuilderResolver resolver, String... aliases) {
            miniTagParserBuilder.registerTag(name, TagType.SINGLE, aliases);
            componentTagResolvers.put(name, resolver);
            return this;
        }

        @NotNull
        @Contract("_, _ -> this")
        public Builder registerPlaceholder(Placeholder placeholder, String... aliases) {
            registerComponentTag(placeholder.getName(), placeholder, aliases);
            return this;
        }

        @NotNull
        @Contract("_, _, _ -> this")
        public Builder putStylingAlias(String name, TransformedTag.Transformer transformer, String... aliases) {
            miniTagParserBuilder.registerTag(name, new TransformedTag(TagType.PAIR, transformer), aliases);
            return this;
        }

        @NotNull
        @Contract("-_, _ > this")
        public Builder putStylingAlias(Pattern pattern, TransformedTag.Transformer transformer) {
            miniTagParserBuilder.registerTag(pattern, new TransformedTag(TagType.PAIR, transformer));
            return this;
        }

        @NotNull
        @Contract("_, _, _ -> this")
        public Builder putComponentAlias(String name, TransformedTag.Transformer transformer, String... aliases) {
            miniTagParserBuilder.registerTag(name, new TransformedTag(TagType.SINGLE, transformer), aliases);
            return this;
        }

        @NotNull
        @Contract("_, _ -> this")
        public Builder putComponentAlias(Pattern pattern, TransformedTag.Transformer transformer) {
            miniTagParserBuilder.registerTag(pattern, new TransformedTag(TagType.SINGLE, transformer));
            return this;
        }

        @NotNull
        @Contract("-> this")
        public Builder defaultStylingTags() {
            // colors
            registerStylingTag("color", new ColorResolver(), "colour", "c");

            var colorTransformer = new TagToAttributeTransformer("color");
            putStylingAlias("black", colorTransformer);
            putStylingAlias("dark_blue", colorTransformer);
            putStylingAlias("dark_green", colorTransformer);
            putStylingAlias("dark_aqua", colorTransformer);
            putStylingAlias("dark_red", colorTransformer);
            putStylingAlias("dark_purple", colorTransformer);
            putStylingAlias("gold", colorTransformer);
            putStylingAlias("gray", colorTransformer, "grey");
            putStylingAlias("dark_gray", colorTransformer, "dark_grey");
            putStylingAlias("blue", colorTransformer);
            putStylingAlias("green", colorTransformer);
            putStylingAlias("aqua", colorTransformer);
            putStylingAlias("red", colorTransformer);
            putStylingAlias("light_purple", colorTransformer);
            putStylingAlias("yellow", colorTransformer);
            putStylingAlias("white", colorTransformer);
            putStylingAlias(Pattern.compile("#([\\dA-Fa-f]{6}|[\\dA-Fa-f]{3})"), colorTransformer);

            // decorations
            registerStylingTag("bold", new BoldResolver(), "b");
            registerStylingTag("italic", new ItalicResolver(), "i", "em");
            registerStylingTag("underlined", new UnderlinedResolver(), "u");
            registerStylingTag("strikethrough", new StrikethroughResolver(), "st");
            registerStylingTag("obfuscated", new ObfuscatedResolver(), "obf");

            var negatedDecorationTransformer =  new NegatedDecorationTransformer();
            putStylingAlias("!bold", negatedDecorationTransformer, "!b");
            putStylingAlias("!italic", negatedDecorationTransformer, "!i", "!em");
            putStylingAlias("!underlined", negatedDecorationTransformer, "!u");
            putStylingAlias("!strikethrough", negatedDecorationTransformer, "!st");
            putStylingAlias("!obfuscated", negatedDecorationTransformer, "!obf");

            // misc

            registerStylingTag("click", new ClickResolver());
            registerStylingTag("hover", new HoverResolver());
            registerStylingTag("insertion", new InsertionResolver());
            //registerStylingTag("rainbow", new RainbowResolver()); // TODO
            //registerStylingTag("gradient", new GradientResolver()); // TODO
            //registerStylingTag("transition", new TransitionResolver()); // TODO
            registerStylingTag("font", new FontResolver());

            return this;
        }

        @NotNull
        @Contract("-> this")
        public Builder defaultComponentTags() {
            registerComponentTag("selector", new SelectorResolver(), "sel");
            registerComponentTag("lang", new TranslatableResolver(), "tr", "translate");
            registerComponentTag("key", new KeybindResolver());
            registerComponentTag("score", new ScoreResolver());
            registerComponentTag("legacy", new LegacyResolver()); // for internal reasons
            registerPlaceholder(Placeholder.component("newline", Component.newLine()), "br");
            return this;
        }

        @NotNull
        @Contract(value = "-> new", pure = true)
        public MiniMessageParser build() {
            return new MiniMessageParser(miniTagParserBuilder.build(), Map.copyOf(componentTagResolvers), Map.copyOf(componentStylingResolvers));
        }
    }
}
