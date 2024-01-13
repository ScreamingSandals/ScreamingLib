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

package org.screamingsandals.lib.minitag;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.minitag.nodes.Node;
import org.screamingsandals.lib.minitag.nodes.RootNode;
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.minitag.nodes.TextNode;
import org.screamingsandals.lib.minitag.tags.RegisteredTag;
import org.screamingsandals.lib.minitag.tags.StandardTag;
import org.screamingsandals.lib.minitag.tags.TagType;
import org.screamingsandals.lib.minitag.tags.TransformedTag;

import java.util.*;
import java.util.regex.Pattern;

@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor
@Data
public class MiniTagParser {
    public static final char ESCAPE_SYMBOL = '\\';
    public static final char TAG_OPENING_SYMBOL = '<';
    public static final char TAG_CLOSING_SYMBOL = '>';
    public static final char ENDING_TAG_SYMBOL = '/';
    public static final char QUOTE = '"';
    public static final char ALTERNATE_QUOTE = '\'';
    public static final char ARGUMENT_SEPARATOR = ':';
    public static final @NotNull String RESET_TAG = "reset";
    public static final @NotNull String PRE_TAG = "pre";
    public static final @NotNull TagType UNKNOWN_TAG_TYPE = TagType.SINGLE;

    private final boolean strictClosing;
    private final boolean escapeInvalidEndings;
    private final @Nullable String preTag;
    private final @Nullable String resetTag;
    private final @NotNull TagType unknownTagType;
    private final char escapeSymbol;
    private final char tagOpeningSymbol;
    private final char tagClosingSymbol;
    private final char endingTagSymbol;
    private final char argumentSeparator;
    private final @NotNull List<@NotNull Character> quotes;
    private final @NotNull Map<@NotNull String, RegisteredTag> registeredTags;
    private final @NotNull Map<@NotNull Pattern, RegisteredTag> registeredRegexTags;

    public @NotNull RootNode parse(@NotNull String tag) {

        // First iteration: parse the string
        var chars = tag.toCharArray();
        var root = new RootNode();
        var escaped = false;
        var tagOpened = false;
        var inQuotes = false;
        var inPre = false;
        var usedQuote = '"';
        var builder = new StringBuilder();
        @NotNull var cursor = new NodeCursor(null, root);

        for (int i = 0; i < chars.length; i++) {
            var c = chars[i];
            if (inPre) {
                builder.append(c);
                if (builder.toString().endsWith("" + tagOpeningSymbol + endingTagSymbol + preTag + tagClosingSymbol)) {
                    builder.setLength(builder.length() - 3 - preTag.length());
                    inPre = false;
                }
                continue;
            }
            if (escaped) {
                builder.append(c);
                escaped = false;
                continue;
            }
            if (c == escapeSymbol) {
                if (tagOpened) {
                    builder.append(c);
                }
                escaped = true;
                continue;
            }
            if (tagOpened) {
                if (inQuotes) {
                    builder.append(c);
                    if (c == usedQuote) {
                        inQuotes = false;
                    }
                } else if (c == tagClosingSymbol) {
                    var tagString = builder.toString();
                    builder.setLength(0);
                    tagOpened = false;
                    if (tagString.equals(resetTag)) {
                        // reset tag closes everything, so just jump back to root
                        cursor = new NodeCursor(null, root);
                    } else if (tagString.equals(preTag)) {
                        inPre = true;
                    } else if (tagString.charAt(0) == endingTagSymbol) {
                        var stripped = tagString.substring(1);
                        if (!(cursor.node instanceof TagNode)) {
                            if (escapeInvalidEndings) {
                                cursor.node.putChildren(new TextNode(tagOpeningSymbol + tagString + tagClosingSymbol));
                                continue;
                            }
                            throw new IllegalArgumentException("Illegal ending tag " + tagOpeningSymbol + tagString + tagClosingSymbol + " (position " + i + "): No tag is opened");
                        }
                        var ts = ((TagNode) cursor.node).getTag();
                        var shortTs = ts.split(String.valueOf(argumentSeparator))[0];
                        var originalShortTs = shortTs;
                        if (!ts.equals(stripped) && !shortTs.equals(stripped)) {
                            if (strictClosing) {
                                if (escapeInvalidEndings) {
                                    cursor.node.putChildren(new TextNode(tagOpeningSymbol + tagString + tagClosingSymbol));
                                    continue;
                                }
                                throw new IllegalArgumentException("Illegal ending tag " + tagOpeningSymbol + tagString + tagClosingSymbol + " (position " + i + "): Wrong tag is closed! Should be " + tagOpeningSymbol + endingTagSymbol + shortTs + tagClosingSymbol);
                            }
                            // let's try to find the closed tag
                            do {
                                var parent = cursor.cursor;
                                if (parent != null && parent.node instanceof TagNode) {
                                    cursor = parent;
                                    ts = ((TagNode) cursor.node).getTag();
                                    shortTs = ts.split(String.valueOf(argumentSeparator))[0];
                                } else {
                                    if (escapeInvalidEndings) {
                                        cursor.node.putChildren(new TextNode(tagOpeningSymbol + tagString + tagClosingSymbol));
                                        continue;
                                    }
                                    throw new IllegalArgumentException("Illegal ending tag " + tagOpeningSymbol + tagString + tagClosingSymbol + " (position " + i + "): Wrong tag is closed! Should be " + tagOpeningSymbol + endingTagSymbol + originalShortTs + tagClosingSymbol);
                                }
                            } while (!ts.equals(stripped) && !shortTs.equals(stripped));
                        }
                        assert cursor.cursor != null;
                        cursor = cursor.cursor;
                    } else {
                        var single = false;
                        if (tagString.charAt(tagString.length() - 1) == endingTagSymbol) {
                            single = true;
                            tagString = tagString.substring(0, tagString.length() - 1);
                        }

                        var tagNode = readTag(tagString);
                        cursor.node.putChildren(tagNode);
                        if (!single) {
                            var register = registeredTags.get(tagNode.getTag());
                            if (register == null && !registeredRegexTags.isEmpty()) {
                                register = registeredRegexTags.entrySet().stream().filter(e -> e.getKey().matcher(tagNode.getTag()).matches()).map(Map.Entry::getValue).findFirst().orElse(null);
                            }
                            var tagType = register != null ? register.tagType() : unknownTagType;
                            if (tagType == TagType.PAIR) {
                                cursor = new NodeCursor(cursor, tagNode);
                            }
                        }
                    }
                } else if (quotes.contains(c)) {
                    inQuotes = true;
                    usedQuote = c;
                    builder.append(c);
                } else {
                    builder.append(c);
                }
            } else {
                if (c == tagOpeningSymbol && i != chars.length - 1 && chars[i + 1] != tagClosingSymbol && chars[i + 1] != tagOpeningSymbol) {
                    var text = builder.toString();
                    if (!text.isEmpty()) {
                        cursor.node.putChildren(new TextNode(builder.toString()));
                    }
                    builder.setLength(0);
                    tagOpened = true;
                } else {
                    builder.append(c);
                }
            }
        }

        if (escaped) {
            builder.append("\\"); // one invalid backslash at the end
        }
        var text = builder.toString();
        if (!text.isEmpty()) {
            cursor.node.putChildren(new TextNode(builder.toString()));
        }

        // Second iteration: merge children text nodes if they can be merged
        mergeTextChildren(root);

        // Third iteration: transform tag names and attributes
        return transformAliases(root);
    }

    @Contract(value = "-> new", pure = true)
    public @NotNull RootNode newRoot() {
        return new RootNode();
    }

    public @NotNull String serialize(@NotNull RootNode root) {
        var builder = new StringBuilder();
        serializeChildren(builder, root.children(), true);
        return builder.toString();
    }

    private @NotNull List<@NotNull TagNode> serializeChildren(@NotNull StringBuilder builder, @NotNull List<@NotNull Node> children, boolean top) {
        var stillOpenedTags = new ArrayList<TagNode>();
        for (var a = 0; a < children.size(); a++) {
            var child = children.get(a);
            if (!stillOpenedTags.isEmpty()) {
                // we probably don't have strict closing
                if (top && resetTag != null) {
                    builder.append(tagOpeningSymbol).append(resetTag).append(tagClosingSymbol);
                } else {
                    var lastTag = stillOpenedTags.get(stillOpenedTags.size() - 1);
                    if (stillOpenedTags.size() == 1) {
                        builder.append(tagOpeningSymbol).append(endingTagSymbol).append(lastTag.getTag()).append(tagClosingSymbol);
                    } else {
                        var sameName = false;
                        var sameArgs = false;
                        for (var i = 0; i < stillOpenedTags.size() - 1; i++) {
                            var tag = stillOpenedTags.get(i);
                            if (tag.getTag().equals(lastTag.getTag())) {
                                sameName = true;

                                if (tag.getArgs().equals(lastTag.getArgs())) {
                                    sameArgs = true;
                                    break;
                                }
                            }
                        }

                        if (sameArgs) { // gave up, just close everything
                            for (var tag : stillOpenedTags) {
                                builder.append(tagOpeningSymbol).append(endingTagSymbol).append(tag.getTag()).append(tagClosingSymbol);
                            }
                        } else if (sameName) {
                            builder.append(tagOpeningSymbol).append(endingTagSymbol).append(lastTag.getTag());
                            for (var arg : lastTag.getArgs()) {
                                builder.append(argumentSeparator);
                                if (arg.indexOf(tagOpeningSymbol) != -1 || arg.indexOf(tagClosingSymbol) != -1 || arg.indexOf(escapeSymbol) != -1 || arg.indexOf(argumentSeparator) != -1 || arg.indexOf(endingTagSymbol) != -1 || quotes.stream().anyMatch(c -> arg.indexOf(c) != -1)) {
                                    var usedQuote = quotes.get(0);
                                    builder.append(usedQuote);
                                    for (var c : arg.toCharArray()) {
                                        if (c == usedQuote || c == escapeSymbol) {
                                            builder.append(escapeSymbol);
                                        }
                                        builder.append(c);
                                    }
                                    builder.append(usedQuote);
                                } else {
                                    builder.append(arg);
                                }
                            }
                            builder.append(tagClosingSymbol);
                        } else {
                            builder.append(tagOpeningSymbol).append(endingTagSymbol).append(lastTag.getTag()).append(tagClosingSymbol);
                        }
                    }
                }
                stillOpenedTags.clear();
            }

            if (child instanceof TextNode) {
                for (var c : ((TextNode) child).getText().toCharArray()) {
                    if (c == tagOpeningSymbol) {
                        builder.append(escapeSymbol);
                    }
                    builder.append(c);
                }
            } else if (child instanceof TagNode) {
                builder.append(tagOpeningSymbol).append(((TagNode) child).getTag());
                for (var arg : ((TagNode) child).getArgs()) {
                    builder.append(argumentSeparator);
                    if (arg.indexOf(tagOpeningSymbol) != -1 || arg.indexOf(tagClosingSymbol) != -1 || arg.indexOf(escapeSymbol) != -1 || arg.indexOf(argumentSeparator) != -1 || arg.indexOf(endingTagSymbol) != -1 || quotes.stream().anyMatch(c -> arg.indexOf(c) != -1)) {
                        var usedQuote = quotes.get(0);
                        builder.append(usedQuote);
                        for (var c : arg.toCharArray()) {
                            if (c == usedQuote || c == escapeSymbol) {
                                builder.append(escapeSymbol);
                            }
                            builder.append(c);
                        }
                        builder.append(usedQuote);
                    } else {
                        builder.append(arg);
                    }
                }
                if (!child.hasChildren() && (strictClosing || !top || (a + 1) != children.size())) { // don't ask me what is this
                    builder.append(endingTagSymbol);
                }
                builder.append(tagClosingSymbol);
                if (child.hasChildren()) {
                    var stillOpenedTagsChild = serializeChildren(builder, child.children(), false);
                    if (strictClosing) {
                        builder.append(tagOpeningSymbol).append(endingTagSymbol).append(((TagNode) child).getTag()).append(tagClosingSymbol);
                    } else {
                        stillOpenedTags.addAll(stillOpenedTagsChild);
                        stillOpenedTags.add((TagNode) child);
                    }
                }
            } else {
                throw new IllegalArgumentException("Invalid node type: " + child.getClass().getName());
            }
        }
        return stillOpenedTags;
    }

    private @NotNull TagNode readTag(@NotNull String tag) {
        if (!tag.contains(":")) {
            return new TagNode(tag, List.of());
        } else {
            var chars = tag.toCharArray();

            var escaped = false;
            var inQuotes = false;
            var usedQuote = '"';
            var arguments = new ArrayList<String>();
            var builder = new StringBuilder();

            for (char c : chars) {
                if (escaped) {
                    builder.append(c);
                    escaped = false;
                    continue;
                }
                if (c == escapeSymbol) {
                    escaped = true;
                    continue;
                }

                if (inQuotes) {
                    if (c == usedQuote) {
                        inQuotes = false;
                    } else {
                        builder.append(c);
                    }
                } else if (c == argumentSeparator) {
                    arguments.add(builder.toString());
                    builder.setLength(0);
                } else if (builder.length() == 0 && quotes.contains(c)) {
                    inQuotes = true;
                    usedQuote = c;
                } else {
                    builder.append(c);
                }
            }

            if (builder.length() != 0) {
                arguments.add(builder.toString());
            }

            var name = arguments.get(0);
            arguments.remove(0);
            return new TagNode(name, arguments);
        }
    }

    private void mergeTextChildren(@NotNull Node node) {
        if (node.children().isEmpty()) {
            return;
        }

        if (node.children().size() == 1) {
            var onlyNode = node.children().get(0);
            if (!(onlyNode instanceof TextNode)) {
                mergeTextChildren(onlyNode);
            }
            return;
        }

        var children = List.copyOf(node.children());
        node.children().clear();
        var builder = new StringBuilder();
        for (var child : children) {
            if (child instanceof TextNode) {
                builder.append(((TextNode) child).getText());
            } else {
                if (builder.length() != 0) {
                    node.putChildren(new TextNode(builder.toString()));
                    builder.setLength(0);
                }
                node.putChildren(child);
                mergeTextChildren(child);
            }
        }

        if (builder.length() != 0) {
            node.putChildren(new TextNode(builder.toString()));
            builder.setLength(0);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Node> @NotNull T transformAliases(@NotNull T node) {
        var children = List.copyOf(node.children());
        node.children().clear();
        if (node instanceof TagNode) {
            var registeredTag = registeredTags.get(((TagNode) node).getTag());
            if (registeredTag instanceof TransformedTag) {
                node = (T) ((TransformedTag) registeredTag).transformer().transform((TagNode) node);
            } else {
                var finalNode = (TagNode) node;
                var registeredRegexTag = registeredRegexTags.entrySet().stream().filter(e -> e.getKey().matcher(finalNode.getTag()).matches()).map(Map.Entry::getValue).findFirst();
                if (registeredRegexTag.isPresent() && registeredRegexTag.get() instanceof TransformedTag) {
                    node = (T) ((TransformedTag) registeredRegexTag.get()).transformer().transform((TagNode) node);
                }
            }
        }

        if (!children.isEmpty()) {
            for (var child : children) {
                node.putChildren(transformAliases(child));
            }
        }
        return node;
    }

    @Contract(value = "-> new", pure = true)
    public static @NotNull Builder builder() {
        return new Builder();
    }

    @RequiredArgsConstructor
    private static class NodeCursor {
        private final @Nullable NodeCursor cursor;
        private final @NotNull Node node;
    }

    @Setter
    public static class Builder {
        private boolean strictClosing = true;
        private boolean escapeInvalidEndings;
        private @Nullable String preTag;
        private @Nullable String resetTag = RESET_TAG;
        private @NotNull TagType unknownTagType = UNKNOWN_TAG_TYPE;
        private char escapeSymbol = ESCAPE_SYMBOL;
        private char tagOpeningSymbol = TAG_OPENING_SYMBOL;
        private char tagClosingSymbol = TAG_CLOSING_SYMBOL;
        private char endingTagSymbol = ENDING_TAG_SYMBOL;
        private char argumentSeparator = ARGUMENT_SEPARATOR;
        private @NotNull List<@NotNull Character> quotes = List.of(QUOTE, ALTERNATE_QUOTE);
        private final @NotNull Map<@NotNull String, RegisteredTag> registeredTags = new HashMap<>();
        private final @NotNull Map<@NotNull Pattern, RegisteredTag> registeredRegexTags = new HashMap<>();

        @Tolerate
        @Contract("_ -> this")
        public @NotNull Builder preTag(boolean enablePreTag) {
            preTag = enablePreTag ? PRE_TAG : null;
            return this;
        }

        @Tolerate
        @Contract("_ -> this")
        public @NotNull Builder resetTag(boolean enableResetTag) {
            resetTag = enableResetTag ? RESET_TAG : null;
            return this;
        }

        @Tolerate
        @Contract("_ -> this")
        public @NotNull Builder quotes(@NotNull Character @NotNull... quotes) {
            this.quotes = List.of(quotes);
            return this;
        }

        @Contract("_, _ -> this")
        public @NotNull Builder registerTag(@NotNull String tag, @NotNull RegisteredTag registeredTag) {
            registeredTags.put(tag, registeredTag);
            return this;
        }

        @Contract("_, _ -> this")
        public @NotNull Builder registerTag(@NotNull Pattern tag, @NotNull RegisteredTag registeredTag) {
            registeredRegexTags.put(tag, registeredTag);
            return this;
        }

        @Contract("_, _ -> this")
        public @NotNull Builder registerTag(@NotNull String tag, @NotNull TagType type) {
            registeredTags.put(tag, new StandardTag(type));
            return this;
        }

        @Contract("_, _ -> this")
        public @NotNull Builder registerTag(@NotNull Pattern tag, @NotNull TagType type) {
            registeredRegexTags.put(tag, new StandardTag(type));
            return this;
        }

        @Contract("_, _, _ -> this")
        public @NotNull Builder registerTag(@NotNull String tag, @NotNull RegisteredTag registeredTag, @NotNull String @NotNull... aliases) {
            registeredTags.put(tag, registeredTag);
            for (var alias : aliases) {
                if (registeredTag instanceof TransformedTag) {
                    registeredTags.put(alias, new TransformedTag(registeredTag.tagType(), node -> ((TransformedTag) registeredTag).transformer().transform(new TagNode(tag, node.getArgs()))));
                } else {
                    registeredTags.put(alias, new TransformedTag(registeredTag.tagType(), node -> new TagNode(tag, node.getArgs())));
                }
            }
            return this;
        }

        @Contract("_, _, _ -> this")
        public @NotNull Builder registerTag(@NotNull String tag, @NotNull TagType type, @NotNull String @NotNull... aliases) {
            registeredTags.put(tag, new StandardTag(type));
            for (var alias : aliases) {
                registeredTags.put(alias, new TransformedTag(type, node -> new TagNode(tag, node.getArgs())));
            }
            return this;
        }

        @Contract(value = "-> new", pure = true)
        public @NotNull MiniTagParser build() {
            return new MiniTagParser(
                    strictClosing,
                    escapeInvalidEndings,
                    preTag,
                    resetTag,
                    unknownTagType,
                    escapeSymbol,
                    tagOpeningSymbol,
                    tagClosingSymbol,
                    endingTagSymbol,
                    argumentSeparator,
                    quotes,
                    Map.copyOf(registeredTags),
                    Map.copyOf(registeredRegexTags)
            );
        }
    }
}
