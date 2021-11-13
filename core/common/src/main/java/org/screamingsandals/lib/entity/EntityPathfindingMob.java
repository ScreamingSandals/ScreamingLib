package org.screamingsandals.lib.entity;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface EntityPathfindingMob extends EntityLiving {
    void setCurrentTarget(@Nullable EntityLiving target);

    Optional<EntityLiving> getCurrentTarget();
}
