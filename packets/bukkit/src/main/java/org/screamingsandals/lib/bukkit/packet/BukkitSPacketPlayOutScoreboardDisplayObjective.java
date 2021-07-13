package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutScoreboardDisplayObjective;
import org.screamingsandals.lib.utils.AdventureHelper;

public class BukkitSPacketPlayOutScoreboardDisplayObjective extends BukkitSPacket implements SPacketPlayOutScoreboardDisplayObjective {

    public BukkitSPacketPlayOutScoreboardDisplayObjective() {
        super(ClassStorage.NMS.PacketPlayOutScoreboardDisplayObjective);
    }

    @Override
    public SPacketPlayOutScoreboardDisplayObjective setObjectiveKey(Component objectiveKey) {
        if (objectiveKey == null) {
            throw new UnsupportedOperationException("Objective key cannot be null!");
        }
        packet.setField("b,field_149373_b,f_133128_", AdventureHelper.toLegacy(objectiveKey));
        return this;
    }

    @Override
    public SPacketPlayOutScoreboardDisplayObjective setDisplaySlot(DisplaySlot slot) {
        packet.setField("a,field_149374_a,f_133127_", slot.ordinal());
        return this;
    }
}
