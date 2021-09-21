package org.screamingsandals.lib.block.state;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.Range;

public interface SignHolder extends TileStateHolder {
    Component[] lines();

    Component line(@Range(from = 0, to = 3) int index);

    void line(@Range(from = 0, to = 3) int index, Component component);

    default void line(@Range(from = 0, to = 3) int index, ComponentLike component) {
        line(index, component.asComponent());
    }
}
