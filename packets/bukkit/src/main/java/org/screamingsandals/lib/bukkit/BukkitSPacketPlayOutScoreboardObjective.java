package org.screamingsandals.lib.bukkit;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutScoreboardObjective;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSPacketPlayOutScoreboardObjective extends BukkitSPacket implements SPacketPlayOutScoreboardObjective {

    public BukkitSPacketPlayOutScoreboardObjective() {
        super(ClassStorage.NMS.PacketPlayOutScoreboardObjective);
    }

    @Override
    public void setObjectiveKey(Component objectiveKey) {
        if (objectiveKey == null) {
            throw new UnsupportedOperationException("Objective key cannot be null!");
        }
        packet.setField("a", AdventureHelper.toLegacy(objectiveKey));
    }

    @Override
    public void setTitle(Component title) {
        if (title == null) {
            throw new UnsupportedOperationException("Title cannot be null!");
        }
        if (packet.setField("b", ClassStorage.asMinecraftComponent(title)) == null) {
            packet.setField("b", AdventureHelper.toLegacy(title));
        }
    }

    @Override
    public void setCriteria(Type criteriaType) {
        String enumConstant = "INTEGER";
        switch (criteriaType) {
            case HEARTS:
                enumConstant = "HEARTS";
                break;
            case INTEGER:
                enumConstant = "INTEGER";
                break;
        }
        packet.setField("c", Reflect.findEnumConstant(ClassStorage.NMS.EnumScoreboardHealthDisplay, enumConstant));
    }

    @Override
    public void setMode(Mode mode) {
        int num = 0;
        switch (mode) {
            case CREATE:
                num = 0;
                break;
            case UPDATE:
                num = 2;
                break;
            case DESTROY:
                num = 1;
                break;
        }
        packet.setField("d", num);
    }
}
