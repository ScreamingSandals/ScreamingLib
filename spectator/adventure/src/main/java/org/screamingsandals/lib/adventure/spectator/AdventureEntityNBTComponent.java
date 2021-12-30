package org.screamingsandals.lib.adventure.spectator;

import net.kyori.adventure.text.EntityNBTComponent;

public class AdventureEntityNBTComponent extends AdventureNBTComponent<EntityNBTComponent> {
    public AdventureEntityNBTComponent(EntityNBTComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Target target() {
        return (EntityTarget) () -> ((EntityNBTComponent) wrappedObject).selector();
    }
}
