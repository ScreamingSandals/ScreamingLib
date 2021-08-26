package org.screamingsandals.lib.event.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SCauldronLevelChangeEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<Integer> oldLevel;
    private final ImmutableObjectLink<Reason> reason;
    private final ObjectLink<Integer> newLevel;

    public BlockHolder getBlock() {
        return block.get();
    }

    public EntityBasic getEntity() {
        return entity.get();
    }

    public int getOldLevel() {
        return oldLevel.get();
    }

    public Reason getReason() {
        return reason.get();
    }

    public int getNewLevel() {
        return newLevel.get();
    }

    public void setNewLevel(int newLevel) {
        this.newLevel.set(newLevel);
    }

    // TODO: holder?
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
