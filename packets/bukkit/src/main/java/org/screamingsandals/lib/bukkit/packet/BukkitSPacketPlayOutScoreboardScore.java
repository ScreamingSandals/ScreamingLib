package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutScoreboardScore;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSPacketPlayOutScoreboardScore extends BukkitSPacket implements SPacketPlayOutScoreboardScore {

    public BukkitSPacketPlayOutScoreboardScore() {
        super(ClassStorage.NMS.PacketPlayOutScoreboardScore);
    }

    @Override
    public SPacketPlayOutScoreboardScore setValue(Component value) {
        if (value == null) {
            throw new UnsupportedOperationException("Value cannot be null!");
        }
        packet.setField("a,field_149329_a", AdventureHelper.toLegacy(value));
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardScore setObjectiveKey(Component objectiveKey) {
        if (objectiveKey == null) {
            throw new UnsupportedOperationException("Objective key cannot be null!");
        }
        packet.setField("b,field_149327_b", AdventureHelper.toLegacy(objectiveKey));
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardScore setScore(int score) {
        packet.setField("c,field_149328_c", score);
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardScore setAction(ScoreboardAction action) {
        if (action == null) {
            throw new UnsupportedOperationException("Action cannot be null!");
        }
        String enumConstant = action.name().toUpperCase();
        packet.setField("d,field_149326_d", Reflect.findEnumConstant(ClassStorage.NMS.EnumScoreboardAction, enumConstant));
        return this;
    }
}
