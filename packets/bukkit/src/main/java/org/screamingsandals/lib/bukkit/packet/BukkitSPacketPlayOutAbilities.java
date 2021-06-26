package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutAbilities;

public class BukkitSPacketPlayOutAbilities extends BukkitSPacket implements SPacketPlayOutAbilities {
    public BukkitSPacketPlayOutAbilities() {
        super(ClassStorage.NMS.PacketPlayOutAbilities);
    }

    @Override
    public void setInvulnerable(boolean invulnerable) {
        if (packet.setField("a", invulnerable) == null) {
            packet.setField("e", invulnerable);
        }
    }

    @Override
    public void setFlying(boolean isFlying) {
        if (packet.setField("b", isFlying) == null) {
            packet.setField("f", isFlying);
        }
    }

    @Override
    public void setCanFly(boolean canFly) {
       if (packet.setField("c", canFly) == null) {
           packet.setField("g", canFly);
       }
    }

    @Override
    public void setCanInstantlyBuild(boolean canInstantlyBuild) {
        if (packet.setField("d", canInstantlyBuild) == null) {
            packet.setField("h", canInstantlyBuild);
        }
    }

    @Override
    public void setFlyingSpeed(float speed) {
        if (packet.setField("e", speed) == null) {
            packet.setField("i", speed);
        }
    }

    @Override
    public void setWalkingSpeed(float walkingSpeed) {
        if (packet.setField("f", walkingSpeed) == null) {
            packet.setField("j", walkingSpeed);
        }
    }
}
