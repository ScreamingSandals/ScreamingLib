package org.screamingsandals.lib.minitag.nodes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class TextNode extends Node {
    private final String text;

    @Override
    public String toString() {
        return '\"' + text + '\"';
    }

    @Override
    public void putChildren(@NotNull Node node) {
        throw new UnsupportedOperationException("Can't add children to text!");
    }
}
