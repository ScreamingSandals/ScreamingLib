package org.screamingsandals.lib.packet;

public interface SPacketPlayOutAbilities extends SPacket {

    SPacketPlayOutAbilities setInvulnerable(boolean invulnerable);

    SPacketPlayOutAbilities setFlying(boolean isFlying);

    SPacketPlayOutAbilities setCanFly(boolean canFly);

    SPacketPlayOutAbilities setCanInstantlyBuild(boolean canInstantlyBuild);

    SPacketPlayOutAbilities setFlyingSpeed(float speed);

    SPacketPlayOutAbilities setWalkingSpeed(float walkingSpeed);
}
