package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutAbilities;

public class BukkitSPacketPlayOutAbilities extends BukkitSPacket implements SPacketPlayOutAbilities {
    public BukkitSPacketPlayOutAbilities() {
        super(ClassStorage.NMS.PacketPlayOutAbilities);
    }

    @Override
    public void setInvulnerable(boolean invulnerable) {
        packet.setField("a", invulnerable);
    }

    @Override
    public void setFlying(boolean isFlying) {
        packet.setField("b", isFlying);
    }

    @Override
    public void setCanFly(boolean canFly) {
        packet.setField("c", canFly);
    }

    @Override
    public void setCanInstantlyBuild(boolean canInstantlyBuild) {
        packet.setField("d", canInstantlyBuild);
    }

    @Override
    public void setFlyingSpeed(float speed) {
        packet.setField("e", speed);
    }

    @Override
    public void setWalkingSpeed(float walkingSpeed) {
        packet.setField("f", walkingSpeed);
    }
}
