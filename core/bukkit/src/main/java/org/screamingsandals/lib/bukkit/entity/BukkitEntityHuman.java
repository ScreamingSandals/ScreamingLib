package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.HumanEntity;
import org.screamingsandals.lib.entity.EntityHuman;

public class BukkitEntityHuman extends BukkitEntityLiving implements EntityHuman {
    protected BukkitEntityHuman(HumanEntity wrappedObject) {
        super(wrappedObject);
    }
}
