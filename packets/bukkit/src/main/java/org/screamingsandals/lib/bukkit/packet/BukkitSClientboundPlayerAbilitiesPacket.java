package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.nms.accessors.ClientboundPlayerAbilitiesPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundPlayerAbilitiesPacket;

public class BukkitSClientboundPlayerAbilitiesPacket extends BukkitSPacket implements SClientboundPlayerAbilitiesPacket {

    public BukkitSClientboundPlayerAbilitiesPacket() {
        super(ClientboundPlayerAbilitiesPacketAccessor.getType());
    }

    @Override
    public SClientboundPlayerAbilitiesPacket setInvulnerable(boolean invulnerable) {
        packet.setField(ClientboundPlayerAbilitiesPacketAccessor.getFieldInvulnerable(), invulnerable);
        return this;
    }

    @Override
    public SClientboundPlayerAbilitiesPacket setFlying(boolean isFlying) {
        packet.setField(ClientboundPlayerAbilitiesPacketAccessor.getFieldIsFlying(), isFlying);
        return this;
    }

    @Override
    public SClientboundPlayerAbilitiesPacket setCanFly(boolean canFly) {
        packet.setField(ClientboundPlayerAbilitiesPacketAccessor.getFieldCanFly(), canFly);
        return this;
    }

    @Override
    public SClientboundPlayerAbilitiesPacket setCanInstantlyBuild(boolean canInstantlyBuild) {
        packet.setField(ClientboundPlayerAbilitiesPacketAccessor.getFieldInstabuild(), canInstantlyBuild);
        return this;
    }

    @Override
    public SClientboundPlayerAbilitiesPacket setFlyingSpeed(float speed) {
        packet.setField(ClientboundPlayerAbilitiesPacketAccessor.getFieldFlyingSpeed(), speed);
        return this;
    }

    @Override
    public SClientboundPlayerAbilitiesPacket setWalkingSpeed(float walkingSpeed) {
        packet.setField(ClientboundPlayerAbilitiesPacketAccessor.getFieldWalkingSpeed(), walkingSpeed);
        return this;
    }
}
