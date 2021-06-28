package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutAbilities;

public class BukkitSPacketPlayOutAbilities extends BukkitSPacket implements SPacketPlayOutAbilities {
    public BukkitSPacketPlayOutAbilities() {
        super(ClassStorage.NMS.PacketPlayOutAbilities);
    }

    @Override
    public void setInvulnerable(boolean invulnerable) {
        if (Version.isVersion(1, 17)) {
            packet.setField("e", invulnerable);
        } else {
            packet.setField("a,field_149119_a", invulnerable);
        }
    }

    @Override
    public void setFlying(boolean isFlying) {
        if (Version.isVersion(1, 17)) {
            packet.setField("f", isFlying);
        } else {
            packet.setField("b,field_149117_b", isFlying);
        }
    }

    @Override
    public void setCanFly(boolean canFly) {
        if (Version.isVersion(1, 17)) {
            packet.setField("g", canFly);
        } else {
            packet.setField("c,field_149118_c", canFly);
        }
    }

    @Override
    public void setCanInstantlyBuild(boolean canInstantlyBuild) {
        if (Version.isVersion(1, 17)) {
            packet.setField("h", canInstantlyBuild);
        } else {
            packet.setField("d,field_149115_d", canInstantlyBuild);
        }
    }

    @Override
    public void setFlyingSpeed(float speed) {
        if (Version.isVersion(1, 17)) {
            packet.setField("i", speed);
        } else {
            packet.setField("e,field_149116_e", speed);
        }
    }

    @Override
    public void setWalkingSpeed(float walkingSpeed) {
        if (Version.isVersion(1, 17)) {
            packet.setField("j", walkingSpeed);
        } else {
            packet.setField("f,field_149114_f", walkingSpeed);
        }
    }
}
