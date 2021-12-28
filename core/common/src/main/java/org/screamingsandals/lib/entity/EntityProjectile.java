package org.screamingsandals.lib.entity;

import org.jetbrains.annotations.Nullable;

public interface EntityProjectile extends EntityBasic {
    @Nullable
    ProjectileShooter getShooter();

    void setShooter(ProjectileShooter shooter);

    boolean doesBounce();

    void setBounce(boolean bounce);
}
