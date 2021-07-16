package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundSetScorePacketAccessor;
import org.screamingsandals.lib.nms.accessors.ServerScoreboard_i_MethodAccessor;
import org.screamingsandals.lib.packet.SClientboundSetScorePacket;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSClientboundSetScorePacket extends BukkitSPacket implements SClientboundSetScorePacket {

    public BukkitSClientboundSetScorePacket() {
        super(ClientboundSetScorePacketAccessor.getType());
    }

    @Override
    public SClientboundSetScorePacket setValue(Component value) {
        if (value == null) {
            throw new UnsupportedOperationException("Value cannot be null!");
        }

        packet.setField(ClientboundSetScorePacketAccessor.getFieldOwner(), AdventureHelper.toLegacy(value));
        return this;
    }

    @Override
    public SClientboundSetScorePacket setObjectiveKey(Component objectiveKey) {
        if (objectiveKey == null) {
            throw new UnsupportedOperationException("Objective key cannot be null!");
        }
        packet.setField(ClientboundSetScorePacketAccessor.getFieldObjectiveName(), AdventureHelper.toLegacy(objectiveKey));
        return this;
    }

    @Override
    public SClientboundSetScorePacket setScore(int score) {
        packet.setField(ClientboundSetScorePacketAccessor.getFieldScore(), score);
        return this;
    }

    @Override
    public SClientboundSetScorePacket setAction(ScoreboardAction action) {
        if (action == null) {
            throw new UnsupportedOperationException("Action cannot be null!");
        }
        String enumConstant = action.name().toUpperCase();
        packet.setField(ClientboundSetScorePacketAccessor.getFieldMethod(), Reflect.findEnumConstant(ServerScoreboard_i_MethodAccessor.getType(), enumConstant));
        return this;
    }
}
