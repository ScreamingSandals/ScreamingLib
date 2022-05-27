package org.screamingsandals.lib.test.minitag;

import org.junit.jupiter.api.Test;
import org.screamingsandals.lib.minitag.MiniTagParser;
import org.screamingsandals.lib.minitag.nodes.RootNode;
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.minitag.nodes.TextNode;
import org.screamingsandals.lib.minitag.tags.TagType;
import org.screamingsandals.lib.minitag.tags.TransformedTag;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class MiniTagParserTest {
    @Test
    public void testParseTag() {
        var parser = MiniTagParser.builder()
                .registerTag("yellow", new TransformedTag(TagType.PAIR, node -> new TagNode("color", List.of(node.getTag()))))
                .registerTag("red", new TransformedTag(TagType.PAIR, node -> new TagNode("color", List.of(node.getTag()))))
                .build();

        var result = parser.parse("<yellow>Something <red>Red<reset>Not Colored");

        var expected = new RootNode();

        var yellow = new TagNode("color", List.of("yellow"));
        yellow.putChildren(new TextNode("Something "));
        expected.putChildren(yellow);

        var red = new TagNode("color", List.of("red"));
        red.putChildren(new TextNode("Red"));
        yellow.putChildren(red);

        expected.putChildren(new TextNode("Not Colored"));

        assertEquals(expected, result);
    }

    @Test
    public void testRegex() {
        var parser = MiniTagParser.builder()
                .registerTag(Pattern.compile("#([\\dA-Fa-f]{6}|[\\dA-Fa-f]{3})"), new TransformedTag(TagType.PAIR, node -> new TagNode("color", List.of(node.getTag()))))
                .build();

        var result = parser.parse("<#7755EE>Colored text");

        var expected = new RootNode();
        var color = new TagNode("color", List.of("#7755EE"));
        color.putChildren(new TextNode("Colored text"));
        expected.putChildren(color);

        assertEquals(expected, result);
    }

    @Test
    public void testInvalidTags() {
        var parser = MiniTagParser.builder()
                .build();

        var result = parser.parse("<<<tag>>>>  <>");

        var expected = new RootNode();
        expected.putChildren(new TextNode("<<"));
        expected.putChildren(new TagNode("tag", null));
        expected.putChildren(new TextNode(">>>  <>"));

        assertEquals(expected, result);
    }
}
