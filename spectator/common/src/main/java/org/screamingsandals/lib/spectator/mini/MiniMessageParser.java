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
import org.screamingsandals.lib.minitag.nodes.RootNode;
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.minitag.nodes.TextNode;
import org.screamingsandals.lib.minitag.tags.TagType;
import org.screamingsandals.lib.minitag.tags.TransformedTag;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.TextComponent;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;
import org.screamingsandals.lib.spectator.mini.placeholders.StringLikePlaceholder;
import org.screamingsandals.lib.spectator.mini.resolvers.*;
import org.screamingsandals.lib.spectator.mini.transformers.NegatedDecorationTransformer;
import org.screamingsandals.lib.spectator.mini.transformers.TagToAttributeTransformer;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class MiniMessageParser {

    public static final @NotNull MiniMessageParser INSTANCE = MiniMessageParser.builder()
            .defaultStylingTags()
            .defaultComponentTags()
            .resetTag(true)
            .strictClosing(false)
            .build();
    private final @NotNull MiniTagParser parser;
    private final @NotNull MiniTagParser placeholderOnlyParser;
    private final @NotNull Map<@NotNull String, SingleTagResolver> singleTagResolvers;
    private final @NotNull Map<@NotNull String, DoubleTagResolver> doubleTagResolvers;

    public @NotNull Component parse(@NotNull String str, @NotNull Placeholder... placeholders) {
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
    @ApiStatus.Internal
    public <B extends Component.Builder<B, C>, C extends Component> @NotNull B parseIntoBuilder(@NotNull String str, @NotNull Placeholder... placeholders) {
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

    @ApiStatus.Internal
    public @NotNull String resolvePlaceholdersInString(@NotNull String str, @NotNull Placeholder @NotNull... placeholders) {
        // ignore placeholders that can't be used in strings
        var stringlikePlaceholders = Arrays.stream(placeholders)
                .filter(placeholder -> placeholder instanceof StringLikePlaceholder)
                .toArray(StringLikePlaceholder[]::new);
        return resolvePlaceholdersInString(str, stringlikePlaceholders);
    }

    @ApiStatus.Internal
    public @NotNull String resolvePlaceholdersInString(@NotNull String str, @NotNull StringLikePlaceholder @NotNull... placeholders) {
        var resolved = placeholderOnlyParser.parse(str);

        if (!resolved.hasChildren()) {
            return str;
        }

        var builder = new StringBuilder();

        for (var child : resolved.children()) {
            resolveStringChildren(child, builder, placeholders);
        }

        return builder.toString();
    }

    private void resolveStringChildren(@NotNull Node node, @NotNull StringBuilder builder, @NotNull StringLikePlaceholder... placeholders) {
        if (node instanceof TextNode) {
            builder.append(((TextNode) node).getText());
        } else if (node instanceof TagNode) {
            if (node.hasChildren()) {
                // wtf? there should not be any double tags
                var resolved = node.children();

                for (var child : resolved) {
                    resolveStringChildren(child, builder, placeholders);
                }
            } else {
                for (var p : placeholders) {
                    if (p.getName().equals(((TagNode) node).getTag())) {
                        builder.append(p.getStringResult(this, ((TagNode) node).getArgs(), placeholders));
                        return;
                    }
                }
                builder.append(parser.tagOpeningSymbol())
                        .append(((TagNode) node).getTag())
                        .append(!((TagNode) node).getArgs().isEmpty() ? ":" : "")
                        .append(String.join(":", ((TagNode) node).getArgs()))
                        .append(parser.tagClosingSymbol());
            }
        } else {
            throw new IllegalArgumentException("Unknown node type!");
        }
    }

    @SuppressWarnings("unchecked")
    private <B extends Component.Builder<B, C>, C extends Component> B resolveChildren(Node node, @NotNull Placeholder... placeholders) {
        if (node instanceof TextNode) {
            return (B) Component.text().content(((TextNode) node).getText());
        } else if (node instanceof TagNode) {
            if (singleTagResolvers.containsKey(((TagNode) node).getTag().toLowerCase(Locale.ROOT))) {
                if (node.hasChildren()) {
                    throw new IllegalArgumentException("Tags defining non-text components can't be pair tags!");
                }
                B comp = singleTagResolvers.get(((TagNode) node).getTag().toLowerCase(Locale.ROOT)).resolve(this, (TagNode) node, placeholders);
                if (comp != null) {
                    return comp;
                }
                // if null, fall into placeholders
            } else if (doubleTagResolvers.containsKey(((TagNode) node).getTag().toLowerCase(Locale.ROOT))) {
                var res = doubleTagResolvers.get(((TagNode) node).getTag().toLowerCase(Locale.ROOT));

                if (node.hasChildren()) {
                    var resolved = node.children();
                    if (resolved.size() == 1) {
                        var child = (B) resolveChildren(resolved.get(0), placeholders);
                        if (child.hasStyling()) {
                            var component = (B) Component.text();
                            return Objects.requireNonNullElse(res.resolve(this, component, (TagNode) node, placeholders), component).append(child);
                        } else {
                            return Objects.requireNonNullElse(res.resolve(this, child, (TagNode) node, placeholders), child);
                        }
                    } else {
                        var component = (B) Component.text();
                        return Objects.requireNonNullElse(res.resolve(this, component, (TagNode) node, placeholders), component)
                                .append(
                                        resolved
                                                .stream()
                                                .map(n -> resolveChildren(n, placeholders).build())
                                                .collect(Collectors.toList())
                                );
                    }
                } else {
                    var component = (B) Component.text();
                    return Objects.requireNonNullElse(res.resolve(this, component, (TagNode) node, placeholders), component);
                }
                // return
            }
            if (node.hasChildren()) {
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
            }
            return (B) Component.text()
                    .content(parser.tagOpeningSymbol()
                            + ((TagNode) node).getTag()
                            + (!((TagNode) node).getArgs().isEmpty() ? ":" : "")
                            + String.join(":", ((TagNode) node).getArgs())
                            + parser.tagClosingSymbol()
                    );
        } else {
            throw new IllegalArgumentException("Unknown node type!");
        }
    }

    public @NotNull String serialize(@NotNull Component component) {
        var root = new RootNode();
        visitComponent(root, component);
        return parser.serialize(root);
    }

    private void visitComponent(@NotNull Node parent, @NotNull Component component) {
        TagNode tag = null;
        for (var resolver : doubleTagResolvers.entrySet()) {
            var t = resolver.getValue().serialize(this, resolver.getKey(), component);
            if (t != null) {
                Objects.requireNonNullElse(tag, parent).putChildren(t);
                tag = t;
            }
        }
        var serialized = false;
        for (var resolver : singleTagResolvers.entrySet()) {
            var t = resolver.getValue().serialize(this, resolver.getKey(), component);
            if (t != null) {
                Objects.requireNonNullElse(tag, parent).putChildren(t);
                serialized = true;
                break;
            }
        }
        if (!serialized && component instanceof TextComponent) {
            var content = ((TextComponent) component).content();
            if (!content.isEmpty()) {
                var text = new TextNode(content);
                Objects.requireNonNullElse(tag, parent).putChildren(text);
            }
        }
        for (var child : component.children()) {
            visitComponent(Objects.requireNonNullElse(tag, parent), child);
        }
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final MiniTagParser.@NotNull Builder miniTagParserBuilder = MiniTagParser.builder();
        private final MiniTagParser.@NotNull Builder placeholderOnlyParserBuilder = MiniTagParser.builder()
                .resetTag(false)
                .strictClosing(false)
                .escapeInvalidEndings(true)
                .preTag(false)
                .unknownTagType(TagType.SINGLE);
        private final @NotNull Map<@NotNull String, SingleTagResolver> singleTagResolvers = new HashMap<>();
        private final @NotNull Map<@NotNull String, DoubleTagResolver> doubleTagResolvers = new HashMap<>();

        @Contract("_ -> this")
        public @NotNull Builder strictClosing(boolean strictClosing) {
            miniTagParserBuilder.strictClosing(strictClosing);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder escapeInvalidEndings(boolean escapeInvalidEndings) {
            miniTagParserBuilder.escapeInvalidEndings(escapeInvalidEndings);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder preTag(@Nullable String preTag) {
            miniTagParserBuilder.preTag(preTag);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder preTag(boolean enablePreTag) {
            miniTagParserBuilder.preTag(enablePreTag);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder resetTag(@Nullable String resetTag) {
            miniTagParserBuilder.resetTag(resetTag);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder resetTag(boolean enableResetTag) {
            miniTagParserBuilder.resetTag(enableResetTag);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder escapeSymbol(char escapeSymbol) {
            miniTagParserBuilder.escapeSymbol(escapeSymbol);
            placeholderOnlyParserBuilder.escapeSymbol(escapeSymbol);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder tagOpeningSymbol(char tagOpeningSymbol) {
            miniTagParserBuilder.tagOpeningSymbol(tagOpeningSymbol);
            placeholderOnlyParserBuilder.tagOpeningSymbol(tagOpeningSymbol);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder tagClosingSymbol(char tagClosingSymbol) {
            miniTagParserBuilder.tagClosingSymbol(tagClosingSymbol);
            placeholderOnlyParserBuilder.tagClosingSymbol(tagClosingSymbol);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder endingTagSymbol(char endingTagSymbol) {
            miniTagParserBuilder.endingTagSymbol(endingTagSymbol);
            placeholderOnlyParserBuilder.endingTagSymbol(endingTagSymbol);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder argumentSeparator(char argumentSeparator) {
            miniTagParserBuilder.argumentSeparator(argumentSeparator);
            placeholderOnlyParserBuilder.argumentSeparator(argumentSeparator);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder quotes(List<Character> quotes) {
            miniTagParserBuilder.quotes(quotes);
            placeholderOnlyParserBuilder.quotes(quotes);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Builder quotes(Character... quotes) {
            miniTagParserBuilder.quotes(quotes);
            placeholderOnlyParserBuilder.quotes(quotes);
            return this;
        }

        @Contract("_, _, _ -> this")
        public @NotNull Builder registerStylingTag(String name, DoubleTagResolver resolver, String... aliases) {
            miniTagParserBuilder.registerTag(name, TagType.PAIR, aliases);
            doubleTagResolvers.put(name, resolver);
            return this;
        }

        @Contract("_, _, _ -> this")
        public @NotNull Builder registerComponentTag(String name, SingleTagResolver resolver, String... aliases) {
            miniTagParserBuilder.registerTag(name, TagType.SINGLE, aliases);
            singleTagResolvers.put(name, resolver);
            return this;
        }

        @Contract("_, _ -> this")
        public @NotNull Builder registerPlaceholder(Placeholder placeholder, String... aliases) {
            registerComponentTag(placeholder.getName(), placeholder, aliases);
            return this;
        }

        @Contract("_, _, _ -> this")
        public @NotNull Builder putStylingAlias(String name, TransformedTag.Transformer transformer, String... aliases) {
            miniTagParserBuilder.registerTag(name, new TransformedTag(TagType.PAIR, transformer), aliases);
            return this;
        }

        @Contract("-_, _ > this")
        public @NotNull Builder putStylingAlias(Pattern pattern, TransformedTag.Transformer transformer) {
            miniTagParserBuilder.registerTag(pattern, new TransformedTag(TagType.PAIR, transformer));
            return this;
        }

        @Contract("_, _, _ -> this")
        public @NotNull Builder putComponentAlias(String name, TransformedTag.Transformer transformer, String... aliases) {
            miniTagParserBuilder.registerTag(name, new TransformedTag(TagType.SINGLE, transformer), aliases);
            return this;
        }

        @Contract("_, _ -> this")
        public @NotNull Builder putComponentAlias(Pattern pattern, TransformedTag.Transformer transformer) {
            miniTagParserBuilder.registerTag(pattern, new TransformedTag(TagType.SINGLE, transformer));
            return this;
        }

        @Contract("-> this")
        public @NotNull Builder defaultStylingTags() {
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

            var negatedDecorationTransformer = new NegatedDecorationTransformer();
            putStylingAlias("!bold", negatedDecorationTransformer, "!b");
            putStylingAlias("!italic", negatedDecorationTransformer, "!i", "!em");
            putStylingAlias("!underlined", negatedDecorationTransformer, "!u");
            putStylingAlias("!strikethrough", negatedDecorationTransformer, "!st");
            putStylingAlias("!obfuscated", negatedDecorationTransformer, "!obf");

            // misc

            registerStylingTag("click", new ClickResolver());
            registerStylingTag("hover", new HoverResolver());
            registerStylingTag("insertion", new InsertionResolver(), "insert");
            registerStylingTag("rainbow", new RainbowResolver());
            registerStylingTag("gradient", new GradientResolver());
            registerStylingTag("transition", new TransitionResolver());
            registerStylingTag("font", new FontResolver());

            return this;
        }

        @Contract("-> this")
        public @NotNull Builder defaultComponentTags() {
            registerComponentTag("selector", new SelectorResolver(), "sel");
            registerComponentTag("lang", new TranslatableResolver(), "tr", "translate");
            registerComponentTag("nbt", new NbtResolver(), "data");
            registerComponentTag("key", new KeybindResolver());
            registerComponentTag("score", new ScoreResolver());
            registerPlaceholder(Placeholder.component("newline", Component.newLine()), "br");
            return this;
        }

        @Contract(value = "-> new", pure = true)
        public @NotNull MiniMessageParser build() {
            return new MiniMessageParser(miniTagParserBuilder.build(), placeholderOnlyParserBuilder.build(), Map.copyOf(singleTagResolvers), Map.copyOf(doubleTagResolvers));
        }
    }
}
