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
    public void setUniqueId(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }
        if (Version.isVersion(1, 17)) {
            packet.setField("d", uuid);
        } else {
            packet.setField("a,field_186911_a", uuid);
        }
    }

    @Override
    public void setAction(Action action) {
        if (action == null) {
            throw new UnsupportedOperationException("Action cannot be null!");
        }
        final var enumAction = Reflect.findEnumConstant(ClassStorage.NMS.EnumBossAction, action.name().toUpperCase());
        if (Version.isVersion(1, 17)) {
            packet.setField("e", enumAction);
        } else {
            packet.setField("b,field_186912_b", enumAction);
        }
    }

    @Deprecated
    @Override
    public void setTitle(Component title) {
        if (title == null) {
            title = Component.text("");
        }
        if (packet.setField("c,field_186913_c", ClassStorage.asMinecraftComponent(title)) == null) {
            packet.setField("c,field_186913_c", AdventureHelper.toLegacy(title));
        }
    }

    @Deprecated
    @Override
    public void setHealth(float health) {
        packet.setField("d,field_186914_d", health);
    }

    @Deprecated
    @Override
    public void setColor(Color color) {
        if (color == null) {
            throw new UnsupportedOperationException("Color cannot be null!");
        }
        packet.setField("e,field_186915_e", Reflect.findEnumConstant(ClassStorage.NMS.EnumBarColor, color.name().toUpperCase()));
    }

    @Deprecated
    @Override
    public void setDivision(Division division) {
        if (division == null) {
            throw new UnsupportedOperationException("Division cannot be null!");
        }
        packet.setField("f,field_186916_f", Reflect.findEnumConstant(ClassStorage.NMS.EnumBarStyle, division.name().toUpperCase()));
    }

    @Deprecated
    @Override
    public void setDarkenSky(boolean darkenSky) {
        packet.setField("g,field_186917_g", darkenSky);
    }

    @Deprecated
    @Override
    public void setPlayMusic(boolean playMusic) {
        packet.setField("h,field_186918_h", playMusic);
    }

    @Override
    public void setCreateFog(boolean createFog) {
        packet.setField("i,field_186919_i", createFog);
    }
}
