package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutScoreboardObjective;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSPacketPlayOutScoreboardObjective extends BukkitSPacket implements SPacketPlayOutScoreboardObjective {

    public BukkitSPacketPlayOutScoreboardObjective() {
        super(ClassStorage.NMS.PacketPlayOutScoreboardObjective);
    }

    @Override
    public SPacketPlayOutScoreboardObjective setObjectiveKey(Component objectiveKey) {
        if (objectiveKey == null) {
            throw new UnsupportedOperationException("Objective key cannot be null!");
        }

        final var legacyString = AdventureHelper.toLegacy(objectiveKey);
        if (Version.isVersion(1, 17)) {
            packet.setField("d,field_149327_b", legacyString);
        } else {
            packet.setField("a,field_149343_a", legacyString);
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardObjective setTitle(Component title) {
        if (title == null) {
            throw new UnsupportedOperationException("Title cannot be null!");
        }

        final var minecraftComponent = ClassStorage.asMinecraftComponent(title);
        if (Version.isVersion(1, 17)) {
            packet.setField("e,f_133253_", minecraftComponent);
        } else {
            if (packet.setField("b,field_149341_b", minecraftComponent) == null) {
                packet.setField("b,field_149341_b", AdventureHelper.toLegacy(title));
            }
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardObjective setCriteria(Type criteriaType) {
        if (criteriaType == null) {
            throw new UnsupportedOperationException("CriteriaType cannot be null!");
        }

        final var criteriaEnum = Reflect.findEnumConstant(ClassStorage.NMS.EnumScoreboardHealthDisplay, criteriaType.name().toUpperCase());
        if (Version.isVersion(1, 17)) {
            packet.setField("f,f_133254_", criteriaEnum);
        } else {
            packet.setField("c,field_199857_c", criteriaEnum);
        }
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardObjective setMode(Mode mode) {
        if (mode == null) {
            throw new UnsupportedOperationException("Mode cannot be null!");
        }

        if (Version.isVersion(1, 17)) {
            packet.setField("g,f_133255_", mode.ordinal());
        } else {
            packet.setField("d,field_149342_c", mode.ordinal());
        }
        return this;
    }
}
