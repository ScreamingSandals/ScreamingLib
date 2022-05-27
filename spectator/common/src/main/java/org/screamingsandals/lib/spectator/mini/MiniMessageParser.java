package org.screamingsandals.lib.spectator.mini;

import lombok.Data;
import org.screamingsandals.lib.minitag.MiniTagParser;
import org.screamingsandals.lib.minitag.tags.TagType;
import org.screamingsandals.lib.minitag.tags.TransformedTag;
import org.screamingsandals.lib.spectator.mini.transformers.NegatedDecorationTransformer;
import org.screamingsandals.lib.spectator.mini.transformers.TagToAttributeTransformer;

import java.util.regex.Pattern;

@Data
public class MiniMessageParser {
    private final MiniTagParser parser;

    public MiniMessageParser(MiniTagParser.Builder builder) {
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
                .registerTag("rainbow", TagType.PAIR)
                .registerTag("gradient", TagType.PAIR)
                .registerTag("transition", TagType.PAIR)
                .registerTag("font", TagType.PAIR)
                .registerTag("newline", TagType.SINGLE)

                .build();
    }

    public MiniMessageParser() {
        this(MiniTagParser.builder());
    }


}
