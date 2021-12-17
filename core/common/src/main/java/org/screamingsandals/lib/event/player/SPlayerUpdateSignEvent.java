package org.screamingsandals.lib.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;

public interface SPlayerUpdateSignEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    BlockHolder getBlock();

    Component[] lines();

    Component line(@Range(from = 0, to = 3) int index);

    void line(@Range(from = 0, to = 3) int index, Component component);

    default void line(@Range(from = 0, to = 3) int index, ComponentLike component) {
        line(index, component.asComponent());
    }
}
