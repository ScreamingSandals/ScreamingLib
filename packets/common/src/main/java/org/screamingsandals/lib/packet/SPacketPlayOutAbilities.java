package org.screamingsandals.lib.packet;

public interface SPacketPlayOutAbilities extends SPacket {
    void setInvulnerable(boolean invulnerable);

    void setFlying(boolean isFlying);

    void setCanFly(boolean canFly);

    void setCanInstantlyBuild(boolean canInstantlyBuild);

    void setFlyingSpeed(float speed);

    void setWalkingSpeed(float walkingSpeed);
}
