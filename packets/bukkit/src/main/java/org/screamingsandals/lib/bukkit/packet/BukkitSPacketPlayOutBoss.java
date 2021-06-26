package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
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
        if (packet.setField("a", uuid) == null) {
            packet.setField("d", uuid);
        }
    }

    @Override
    public void setAction(Action action) {
        if (action == null) {
            throw new UnsupportedOperationException("Action cannot be null!");
        }
        final var enumAction = Reflect.findEnumConstant(ClassStorage.NMS.EnumBossAction, action.name().toUpperCase());
        if (packet.setField("b", enumAction) == null) {
            packet.setField("e", enumAction);
        }
    }

    @Override
    public void setTitle(Component title) {
        if (title == null) {
            title = Component.text("");
        }
        if (packet.setField("c", ClassStorage.asMinecraftComponent(title)) == null) {
            packet.setField("c", AdventureHelper.toLegacy(title));
        }
    }

    @Override
    public void setHealth(float health) {
        packet.setField("d", health);
    }

    @Override
    public void setColor(Color color) {
        if (color == null) {
            throw new UnsupportedOperationException("Color cannot be null!");
        }
        packet.setField("e", Reflect.findEnumConstant(ClassStorage.NMS.EnumBarColor, color.name().toUpperCase()));
    }

    @Override
    public void setDivision(Division division) {
        if (division == null) {
            throw new UnsupportedOperationException("Division cannot be null!");
        }
        packet.setField("f", Reflect.findEnumConstant(ClassStorage.NMS.EnumBarStyle, division.name().toUpperCase()));
    }

    @Override
    public void setDarkenSky(boolean darkenSky) {
        packet.setField("g", darkenSky);
    }

    @Override
    public void setPlayMusic(boolean playMusic) {
        packet.setField("h", playMusic);
    }

    @Override
    public void setCreateFog(boolean createFog) {
        packet.setField("i", createFog);
    }
}
