package org.screamingsandals.lib.bukkit;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutScoreboardScore;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSPacketPlayOutScoreboardScore extends BukkitSPacket implements SPacketPlayOutScoreboardScore {

    public BukkitSPacketPlayOutScoreboardScore() {
        super(ClassStorage.NMS.PacketPlayOutScoreboardScore);
    }

    @Override
    public void setValue(Component value) {
        if (value == null) {
            throw new UnsupportedOperationException("Value cannot be null!");
        }
        packet.setField("a", AdventureHelper.toLegacy(value));
    }

    @Override
    public void setObjectiveKey(Component objectiveKey) {
        if (objectiveKey == null) {
            throw new UnsupportedOperationException("Objective key cannot be null!");
        }
        packet.setField("b", AdventureHelper.toLegacy(objectiveKey));
    }

    @Override
    public void setScore(int score) {
        packet.setField("c", score);
    }

    @Override
    public void setAction(ScoreboardAction action) {
        String enumConstant = "CHANGE";
        switch (action) {
            case REMOVE:
                enumConstant = "REMOVE";
            case CHANGE:
                enumConstant = "CHANGE";
                break;
        }
        packet.setField("d", Reflect.findEnumConstant(ClassStorage.NMS.EnumScoreboardAction, enumConstant));
    }
}
