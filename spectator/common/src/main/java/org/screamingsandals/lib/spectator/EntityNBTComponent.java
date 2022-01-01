package org.screamingsandals.lib.spectator;

import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

@LimitedVersionSupport(">= 1.14")
public interface EntityNBTComponent extends NBTComponent {
    String selector();

    interface Builder extends NBTComponent.Builder<Builder, EntityNBTComponent> {
        Builder selector(String selector);
    }
}
