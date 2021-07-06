package org.screamingsandals.lib.event.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SCauldronLevelChangeEvent extends CancellableAbstractEvent {
    private final BlockHolder block;
    private final EntityBasic entity;
    private final int oldLevel;
    private final Reason reason;
    private int newLevel;

    public enum Reason {
        ARMOR_WASH,
        BANNER_WASH,
        BOTTLE_EMPTY,
        BOTTLE_FILL,
        BUCKET_EMPTY,
        BUCKET_FILL,
        EVAPORATE,
        EXTINGUISH,
        NATURAL_FILL,
        SHULKER_WASH,
        UNKNOWN;

        public Reason get(String name) {
            try {
                return valueOf(name.toUpperCase());
            } catch (Throwable ignored) {
                return UNKNOWN;
            }
        }
    }
}
