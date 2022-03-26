package org.screamingsandals.lib.minitag.nodes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class TagNode extends Node {
    private final String tag;
    @Nullable
    private final List<String> args;

    @Override
    @Nullable
    protected String toStringAdditional() {
        return tag + (args != null ? ", " + args : "");
    }
}
