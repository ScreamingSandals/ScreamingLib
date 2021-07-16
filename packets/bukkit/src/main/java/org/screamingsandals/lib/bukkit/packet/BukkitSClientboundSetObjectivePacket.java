package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.nms.accessors.ClientboundSetObjectivePacketAccessor;
import org.screamingsandals.lib.nms.accessors.ObjectiveCriteria_i_RenderTypeAccessor;
import org.screamingsandals.lib.packet.SClientboundSetObjectivePacket;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSClientboundSetObjectivePacket extends BukkitSPacket implements SClientboundSetObjectivePacket {

    public BukkitSClientboundSetObjectivePacket() {
        super(ClientboundSetObjectivePacketAccessor.getType());
    }

    @Override
    public SClientboundSetObjectivePacket setObjectiveKey(Component objectiveKey) {
        if (objectiveKey == null) {
            throw new UnsupportedOperationException("Objective key cannot be null!");
        }

        packet.setField(ClientboundSetObjectivePacketAccessor.getFieldObjectiveName(), AdventureHelper.toLegacy(objectiveKey));
        return this;
    }

    @Override
    public SClientboundSetObjectivePacket setTitle(Component title) {
        if (title == null) {
            throw new UnsupportedOperationException("Title cannot be null!");
        }

        final var minecraftComponent = ClassStorage.asMinecraftComponent(title);
        if (packet.setField(ClientboundSetObjectivePacketAccessor.getFieldDisplayName(), minecraftComponent) == null) {
            packet.setField(ClientboundSetObjectivePacketAccessor.getFieldDisplayName(), AdventureHelper.toLegacy(title));
        }
        return this;
    }

    @Override
    public SClientboundSetObjectivePacket setCriteria(Type criteriaType) {
        if (criteriaType == null) {
            throw new UnsupportedOperationException("CriteriaType cannot be null!");
        }

        final var criteriaEnum = Reflect.findEnumConstant(ObjectiveCriteria_i_RenderTypeAccessor.getType(), criteriaType.name().toUpperCase());
        packet.setField(ClientboundSetObjectivePacketAccessor.getFieldRenderType(), criteriaEnum);
        return this;
    }

    @Override
    public SClientboundSetObjectivePacket setMode(Mode mode) {
        if (mode == null) {
            throw new UnsupportedOperationException("Mode cannot be null!");
        }

        packet.setField(ClientboundSetObjectivePacketAccessor.getFieldMethod(), mode.ordinal());
        return this;
    }
}
