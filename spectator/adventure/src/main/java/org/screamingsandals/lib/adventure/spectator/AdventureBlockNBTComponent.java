package org.screamingsandals.lib.adventure.spectator;

import net.kyori.adventure.text.BlockNBTComponent;

public class AdventureBlockNBTComponent extends AdventureNBTComponent<BlockNBTComponent> {
    public AdventureBlockNBTComponent(BlockNBTComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Target target() {
        return (BlockTarget) () -> ((BlockNBTComponent) wrappedObject).pos().asString();
    }
}
