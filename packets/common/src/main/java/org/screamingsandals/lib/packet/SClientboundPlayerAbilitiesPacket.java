package org.screamingsandals.lib.packet;

public interface SClientboundPlayerAbilitiesPacket extends SPacket {

    SClientboundPlayerAbilitiesPacket setInvulnerable(boolean invulnerable);

    SClientboundPlayerAbilitiesPacket setFlying(boolean isFlying);

    SClientboundPlayerAbilitiesPacket setCanFly(boolean canFly);

    SClientboundPlayerAbilitiesPacket setCanInstantlyBuild(boolean canInstantlyBuild);

    SClientboundPlayerAbilitiesPacket setFlyingSpeed(float speed);

    SClientboundPlayerAbilitiesPacket setWalkingSpeed(float walkingSpeed);
}
