package org.screamingsandals.lib.bukkit.hologram;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ArmorStandAccessor;
import org.screamingsandals.lib.packet.entity.FakeArmorStandEntity;
import org.screamingsandals.lib.world.LocationHolder;

public class HologramPiece extends FakeArmorStandEntity {
    public static final int ENTITY_TYPE_ID;

    static {
        ENTITY_TYPE_ID = ClassStorage.getEntityTypeId("armor_stand", ArmorStandAccessor.getType());
    }

    public HologramPiece(LocationHolder location) {
        super(location, ENTITY_TYPE_ID);
    }
}
