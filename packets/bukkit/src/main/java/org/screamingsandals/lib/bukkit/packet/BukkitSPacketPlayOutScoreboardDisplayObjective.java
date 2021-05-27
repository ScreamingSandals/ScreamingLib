package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.packet.SPacketPlayOutScoreboardDisplayObjective;
import org.screamingsandals.lib.utils.AdventureHelper;

public class BukkitSPacketPlayOutScoreboardDisplayObjective extends BukkitSPacket implements SPacketPlayOutScoreboardDisplayObjective {

    public BukkitSPacketPlayOutScoreboardDisplayObjective() {
        super(ClassStorage.NMS.PacketPlayOutScoreboardDisplayObjective);
    }

    @Override
    public void setObjectiveKey(Component objectiveKey) {
        if (objectiveKey == null) {
            throw new UnsupportedOperationException("Objective key cannot be null!");
        }
        packet.setField("b", AdventureHelper.toLegacy(objectiveKey));
    }

    @Override
    public void setDisplaySlot(DisplaySlot slot) {
        int slotNum = 1;
        switch (slot) {
            case BELOW_NAME:
                slotNum = 2;
                break;
            case SIDEBAR:
                slotNum = 1;
                break;
            case PLAYER_LIST:
                slotNum = 0;
                break;
        }
        packet.setField("a", slotNum);
    }
}
