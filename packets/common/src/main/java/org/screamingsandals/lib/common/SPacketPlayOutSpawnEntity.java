package org.screamingsandals.lib.common;

import org.screamingsandals.lib.entity.EntityLiving;

public interface SPacketPlayOutSpawnEntity {
    void setEntity(EntityLiving entity, int objectData);
}
