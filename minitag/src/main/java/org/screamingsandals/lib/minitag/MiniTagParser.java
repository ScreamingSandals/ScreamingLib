package org.screamingsandals.lib.minitag;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;
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
    public static final String RESET_TAG = "reset";
    public static final String PRE_TAG = "pre";
    public static final TagType UNKNOWN_TAG_TYPE = TagType.SINGLE;

    private final boolean strictClosing;
    private final boolean escapeInvalidEndings;
    @Nullable
    private final String preTag;
    @Nullable
    private final String resetTag;
    private final TagType unknownTagType;
    private final char escapeSymbol;
    private final char tagOpeningSymbol;
    private final char tagClosingSymbol;
    private final char endingTagSymbol;
    private final char argumentSeparator;
    private final List<Character> quotes;
    private final Map<String, RegisteredTag> registeredTags;
    private final Map<Pattern, RegisteredTag> registeredRegexTags;

    public static void main(String[] args) {
        var parser = MiniTagParser.builder()
                .preTag(true)
                .escapeInvalidEndings(false)
                .strictClosing(false)
                .registerTag("red", new TransformedTag(TagType.PAIR, node -> new TagNode("color", List.of(node.getTag()))))
                .build();
        var result = parser.parse("Text <red><hover:show_text:\"<green>cum\">bruh</red> <> my cum \n<reset><yellow/>Cum  on <blue>me!!");
        System.out.println(result);
    }

    public RootNode parse(String tag) {

        // First iteration: parse the string
        var chars = tag.toCharArray();
        var root = new RootNode();
        var escaped = false;
        var tagOpened = false;
        var inQuotes = false;
        var inPre = false;
        var usedQuote = '"';
        var builder = new StringBuilder();
        @NotNull
        var cursor = new NodeCursor(null, root);

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

    private TagNode readTag(String tag) {
        if (!tag.contains(":")) {
            return new TagNode(tag, null);
        } else {
            var chars = tag.toCharArray();

            var escaped = false;
            var inQuotes = false;
            var usedQuote = '"';
            var arguments = new ArrayList<String>();
            var builder = new StringBuilder();

            for (int i = 0; i < chars.length; i++) {
                var c = chars[i];
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

    private void mergeTextChildren(Node node) {
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
                if (builder.length() > 0) {
                    node.putChildren(new TextNode(builder.toString()));
                    builder.setLength(0);
                }
                node.putChildren(child);
                mergeTextChildren(child);
            }
        }

        if (builder.length() > 0) {
            node.putChildren(new TextNode(builder.toString()));
            builder.setLength(0);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Node> T transformAliases(T node) {
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

    public static Builder builder() {
        return new Builder();
    }

    @RequiredArgsConstructor
    private static class NodeCursor {
        @Nullable
        private final NodeCursor cursor;
        @NotNull
        private final Node node;
    }

    @Data
    public static class Builder {
        private boolean strictClosing = true;
        private boolean escapeInvalidEndings = false;
        @Nullable
        private String preTag;
        @Nullable
        private String resetTag = RESET_TAG;
        private TagType unknownTagType = UNKNOWN_TAG_TYPE;
        private char escapeSymbol = ESCAPE_SYMBOL;
        private char tagOpeningSymbol = TAG_OPENING_SYMBOL;
        private char tagClosingSymbol = TAG_CLOSING_SYMBOL;
        private char endingTagSymbol = ENDING_TAG_SYMBOL;
        private char argumentSeparator = ARGUMENT_SEPARATOR;
        private List<Character> quotes = List.of(QUOTE, ALTERNATE_QUOTE);
        private final Map<String, RegisteredTag> registeredTags = new HashMap<>();
        private final Map<Pattern, RegisteredTag> registeredRegexTags = new HashMap<>();

        @NotNull
        public Builder preTag(boolean enablePreTag) {
            preTag = enablePreTag ? PRE_TAG : null;
            return this;
        }

        @NotNull
        public Builder resetTag(boolean enableResetTag) {
            resetTag = enableResetTag ? RESET_TAG : null;
            return this;
        }

        @NotNull
        @Tolerate
        public Builder quotes(Character... quotes) {
            this.quotes = Arrays.asList(quotes);
            return this;
        }

        @NotNull
        public Builder registerTag(String tag, RegisteredTag registeredTag) {
            registeredTags.put(tag, registeredTag);
            return this;
        }

        @NotNull
        public Builder registerTag(Pattern tag, RegisteredTag registeredTag) {
            registeredRegexTags.put(tag, registeredTag);
            return this;
        }

        @NotNull
        public Builder registerTag(String tag, TagType type) {
            registeredTags.put(tag, new StandardTag(type));
            return this;
        }

        @NotNull
        public Builder registerTag(Pattern tag, TagType type) {
            registeredRegexTags.put(tag, new StandardTag(type));
            return this;
        }

        @NotNull
        public Builder registerTag(String tag, RegisteredTag registeredTag, String... aliases) {
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

        @NotNull
        public Builder registerTag(String tag, TagType type, String... aliases) {
            registeredTags.put(tag, new StandardTag(type));
            for (var alias : aliases) {
                registeredTags.put(alias, new TransformedTag(type, node -> new TagNode(tag, node.getArgs())));
            }
            return this;
        }

        public MiniTagParser build() {
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
