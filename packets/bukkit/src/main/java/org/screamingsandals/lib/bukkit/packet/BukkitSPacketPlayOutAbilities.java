package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutAbilities;

public class BukkitSPacketPlayOutAbilities extends BukkitSPacket implements SPacketPlayOutAbilities {

    public BukkitSPacketPlayOutAbilities() {
        super(ClassStorage.NMS.PacketPlayOutAbilities);
    }

    @Override
    public SPacketPlayOutAbilities setInvulnerable(boolean invulnerable) {
        if (Version.isVersion(1, 17)) {
            packet.setField("e,f_132659_", invulnerable);
        } else {
            packet.setField("a,field_149119_a", invulnerable);
        }
        return this;
    }

    @Override
    public SPacketPlayOutAbilities setFlying(boolean isFlying) {
        if (Version.isVersion(1, 17)) {
            packet.setField("f,f_132660_", isFlying);
        } else {
            packet.setField("b,field_149117_b", isFlying);
        }
        return this;
    }

    @Override
    public SPacketPlayOutAbilities setCanFly(boolean canFly) {
        if (Version.isVersion(1, 17)) {
            packet.setField("g,f_132661_", canFly);
        } else {
            packet.setField("c,field_149118_c", canFly);
        }
        return this;
    }

    @Override
    public SPacketPlayOutAbilities setCanInstantlyBuild(boolean canInstantlyBuild) {
        if (Version.isVersion(1, 17)) {
            packet.setField("h,f_132662_", canInstantlyBuild);
        } else {
            packet.setField("d,field_149115_d", canInstantlyBuild);
        }
        return this;
    }

    @Override
    public SPacketPlayOutAbilities setFlyingSpeed(float speed) {
        if (Version.isVersion(1, 17)) {
            packet.setField("i,f_132663_", speed);
        } else {
            packet.setField("e,field_149116_e", speed);
        }
        return this;
    }

    @Override
    public SPacketPlayOutAbilities setWalkingSpeed(float walkingSpeed) {
        if (Version.isVersion(1, 17)) {
            packet.setField("j,f_132664_", walkingSpeed);
        } else {
            packet.setField("f,field_149114_f", walkingSpeed);
        }
        return this;
    }
}
