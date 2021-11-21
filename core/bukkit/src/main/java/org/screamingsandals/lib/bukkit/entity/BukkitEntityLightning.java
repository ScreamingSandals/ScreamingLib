package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.LightningStrike;
import org.screamingsandals.lib.entity.EntityLightning;

public class BukkitEntityLightning extends BukkitEntityBasic implements EntityLightning {
    public BukkitEntityLightning(LightningStrike wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public boolean isEffect() {
        return ((LightningStrike) wrappedObject).isEffect();
    }
}
