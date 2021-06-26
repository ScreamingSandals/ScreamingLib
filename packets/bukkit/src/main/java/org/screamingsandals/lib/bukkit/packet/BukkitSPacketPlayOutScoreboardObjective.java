package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutScoreboardObjective;
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
        final var legacyString = AdventureHelper.toLegacy(objectiveKey);
        if (packet.setField("a", legacyString) == null) {
            packet.setField("d", legacyString);
        }
    }

    @Override
    public void setTitle(Component title) {
        if (title == null) {
            throw new UnsupportedOperationException("Title cannot be null!");
        }

        final var minecraftComponent = ClassStorage.asMinecraftComponent(title);
        if (packet.setField("b", minecraftComponent) == null) {
            if (packet.setField("b", AdventureHelper.toLegacy(title)) == null) {
                packet.setField("e", minecraftComponent);
            }
        }
    }

    @Override
    public void setCriteria(Type criteriaType) {
        if (criteriaType == null) {
            throw new UnsupportedOperationException("CriteriaType cannot be null!");
        }
        final var criteriaEnum = Reflect.findEnumConstant(ClassStorage.NMS.EnumScoreboardHealthDisplay, criteriaType.name().toUpperCase());
        if (packet.setField("c", criteriaEnum) == null) {
            packet.setField("f", criteriaEnum);
        }
    }

    @Override
    public void setMode(Mode mode) {
        if (mode == null) {
            throw new UnsupportedOperationException("Mode cannot be null!");
        }
        if (packet.setField("d", mode.ordinal()) == null) {
            switch (mode) {
                case CREATE:
                    packet.setField("g", 0);
                    break;
                case DESTROY:
                    packet.setField("g", 1);
                    break;
                case UPDATE:
                    packet.setField("g", 2);
                    break;
            }
        }
    }
}
