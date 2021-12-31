package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.packet.entity.FakeArmorStandEntity;
import org.screamingsandals.lib.world.LocationHolder;

public final class HologramPiece extends FakeArmorStandEntity {
    public static final int ENTITY_TYPE_ID = PacketMapper.getArmorStandTypeId();

    public HologramPiece(LocationHolder location) {
        super(location, ENTITY_TYPE_ID);
    }
}
