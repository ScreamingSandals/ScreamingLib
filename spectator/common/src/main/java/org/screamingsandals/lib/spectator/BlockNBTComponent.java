package org.screamingsandals.lib.spectator;

import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

@LimitedVersionSupport(">= 1.14")
public interface BlockNBTComponent extends NBTComponent {
    // TODO: real position API
    String blockPosition();

    interface Builder extends NBTComponent.Builder<Builder, BlockNBTComponent> {
        Builder blockPosition(String blockPosition);
    }
}
