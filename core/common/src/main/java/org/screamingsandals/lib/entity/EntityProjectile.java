package org.screamingsandals.lib.entity;

public interface EntityProjectile extends EntityBasic {
    ProjectileShooter getShooter();

    void setShooter(ProjectileShooter shooter);

    boolean doesBounce();

    void setBounce(boolean bounce);
}
