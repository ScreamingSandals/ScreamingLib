package org.screamingsandals.lib.test.minitag;

import org.junit.jupiter.api.Test;
import org.screamingsandals.lib.minitag.MiniTagParser;
import org.screamingsandals.lib.minitag.nodes.RootNode;
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.minitag.nodes.TextNode;
import org.screamingsandals.lib.minitag.tags.TagType;
import org.screamingsandals.lib.minitag.tags.TransformedTag;

import java.util.List;

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
}
