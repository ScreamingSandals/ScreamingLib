package org.screamingsandals.lib.event.block;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SCauldronLevelChangeEvent extends SCancellableEvent {
    BlockHolder getBlock();

    @Nullable EntityBasic getEntity();

    int getOldLevel();

    Reason getReason();

    int getNewLevel();

    void setNewLevel(int newLevel);

    // TODO: holder?
    enum Reason {
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

        public static Reason get(String name) {
            try {
                return valueOf(name.toUpperCase());
            } catch (Throwable ignored) {
                return UNKNOWN;
            }
        }
    }
}
