package org.screamingsandals.lib.minitag.nodes;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(fluent = true)
public abstract class Node {
    private final List<Node> children = new ArrayList<>();

    public void putChildren(@NotNull Node node) {
        children.add(node);
    }

    @Override
    public String toString() {
        var add = toStringAdditional();
        return this.getClass().getSimpleName().toLowerCase().replace("node", "") + (add != null ? " (" + add + ")" : "") + " {\n" +
                children.stream().map(Node::toString).map(s -> s.replaceAll("(?m)^", "  ")).collect(Collectors.joining(",\n"))
                + "\n}";
    }

    @ApiStatus.OverrideOnly
    @Nullable
    protected String toStringAdditional() {
        return null;
    }
}
