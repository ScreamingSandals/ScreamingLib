package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutBoss;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.UUID;

public class BukkitSPacketPlayOutBoss extends BukkitSPacket implements SPacketPlayOutBoss {
    public BukkitSPacketPlayOutBoss() {
        super(ClassStorage.NMS.PacketPlayOutBoss);
    }

    @Override
    public SPacketPlayOutBoss setUniqueId(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }
        if (Version.isVersion(1, 17)) {
            packet.setField("d,f_131750_", uuid);
        } else {
            packet.setField("a,field_186911_a", uuid);
        }
        return this;
    }

    @Override
    public SPacketPlayOutBoss setAction(Action action) {
        if (action == null) {
            throw new UnsupportedOperationException("Action cannot be null!");
        }
        final var enumAction = Reflect.findEnumConstant(ClassStorage.NMS.EnumBossAction, action.name().toUpperCase());
        if (Version.isVersion(1, 17)) {
            packet.setField("e,f_131751_", enumAction);
        } else {
            packet.setField("b,field_186912_b", enumAction);
        }
        return this;
    }

    @Deprecated
    @Override
    public SPacketPlayOutBoss setTitle(Component title) {
        if (title == null) {
            title = Component.text("");
        }
        if (packet.setField("c,field_186913_c", ClassStorage.asMinecraftComponent(title)) == null) {
            packet.setField("c,field_186913_c", AdventureHelper.toLegacy(title));
        }
        return this;
    }

    @Deprecated
    @Override
    public SPacketPlayOutBoss setHealth(float health) {
        packet.setField("d,field_186914_d", health);
        return this;
    }

    @Deprecated
    @Override
    public SPacketPlayOutBoss setColor(Color color) {
        if (color == null) {
            throw new UnsupportedOperationException("Color cannot be null!");
        }
        packet.setField("e,field_186915_e", Reflect.findEnumConstant(ClassStorage.NMS.EnumBarColor, color.name().toUpperCase()));
        return this;
    }

    @Deprecated
    @Override
    public SPacketPlayOutBoss setDivision(Division division) {
        if (division == null) {
            throw new UnsupportedOperationException("Division cannot be null!");
        }
        packet.setField("f,field_186916_f", Reflect.findEnumConstant(ClassStorage.NMS.EnumBarStyle, division.name().toUpperCase()));
        return this;
    }

    @Deprecated
    @Override
    public SPacketPlayOutBoss setDarkenSky(boolean darkenSky) {
        packet.setField("g,field_186917_g", darkenSky);
        return this;
    }

    @Deprecated
    @Override
    public SPacketPlayOutBoss setPlayMusic(boolean playMusic) {
        packet.setField("h,field_186918_h", playMusic);
        return this;
    }

    @Override
    public SPacketPlayOutBoss setCreateFog(boolean createFog) {
        packet.setField("i,field_186919_i", createFog);
        return this;
    }
}
