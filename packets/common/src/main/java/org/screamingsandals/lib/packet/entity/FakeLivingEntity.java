package org.screamingsandals.lib.packet.entity;

import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.block.BlockPosition;
import org.screamingsandals.lib.packet.MetadataItem;
import org.screamingsandals.lib.world.LocationHolder;

public class FakeLivingEntity extends FakeEntity {

    FakeLivingEntity(LocationHolder location, int typeId) {
        super(location, typeId);

        setHandStates((byte) 0);
        setHealth(1.0f);
        setPotionEffectColor(0);
        setPotionAmbiency(false);
        setBodyArrowCount(0);
        setBeeStingerCount(0);
        setBedPosition(null);
    }

    public void setHandStates(byte handStates) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.HAND_STATES), handStates));
    }

    public void setHealth(float health) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.HEALTH), health));
    }

    /**
     * Sets the potion effect color, 0 if no effect.
     * @param color the color to set
     */
    public void setPotionEffectColor(int color) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.POTION_EFFECT_COLOR), color));
    }

    public void setPotionAmbiency(boolean isAmbient) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.POTION_AMBIENCY), isAmbient));
    }

    public void setBodyArrowCount(int arrowCount) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.BODY_ARROW_COUNT), arrowCount));
    }

    public void setBeeStingerCount(int beeStingerCount) {
        if (Core.isVersion(1,15)) {
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.BEE_STINGER_COUNT), beeStingerCount));
        }
    }

    public void setBedPosition(BlockPosition bedPosition) {
        if (Core.isVersion(1, 14)) {
            put(MetadataItem.ofOpt(EntityMetadata.Registry.getId(EntityMetadata.BED_BLOCK_POSITION), bedPosition));
        }
    }

}
