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
    public void testSerializeTag() {
        var parser = MiniTagParser.builder()
                .strictClosing(false)
                .registerTag("yellow", new TransformedTag(TagType.PAIR, node -> new TagNode("color", List.of(node.getTag()))))
                .registerTag("red", new TransformedTag(TagType.PAIR, node -> new TagNode("color", List.of(node.getTag()))))
                .build();

        var rootNode = new RootNode();

        var yellow = new TagNode("color", List.of("yellow"));
        yellow.putChildren(new TextNode("Something "));
        rootNode.putChildren(yellow);

        var red = new TagNode("color", List.of("red"));
        red.putChildren(new TextNode("Red"));
        yellow.putChildren(red);

        rootNode.putChildren(new TextNode("Not Colored"));

        var result = parser.serialize(rootNode);

        var expected = "<color:yellow>Something <color:red>Red<reset>Not Colored";

        assertEquals(expected, result);
    }

    @Test
    public void testSerializeTagStrict() {
        var parser = MiniTagParser.builder()
                .strictClosing(true)
                .registerTag("yellow", new TransformedTag(TagType.PAIR, node -> new TagNode("color", List.of(node.getTag()))))
                .registerTag("red", new TransformedTag(TagType.PAIR, node -> new TagNode("color", List.of(node.getTag()))))
                .build();

        var rootNode = new RootNode();

        var yellow = new TagNode("color", List.of("yellow"));
        yellow.putChildren(new TextNode("Something "));
        rootNode.putChildren(yellow);

        var red = new TagNode("color", List.of("red"));
        red.putChildren(new TextNode("Red"));
        yellow.putChildren(red);

        rootNode.putChildren(new TextNode("Not Colored"));

        var result = parser.serialize(rootNode);

        var expected = "<color:yellow>Something <color:red>Red</color></color>Not Colored";

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
        expected.putChildren(new TagNode("tag", List.of()));
        expected.putChildren(new TextNode(">>>  <>"));

        assertEquals(expected, result);
    }
}
