package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.entity.Player;
import org.screamingsandals.lib.entity.EntityHuman;

public class MinestomEntityHuman extends MinestomEntityLiving implements EntityHuman {
    protected MinestomEntityHuman(Player wrappedObject) {
        super(wrappedObject);
    }
}
