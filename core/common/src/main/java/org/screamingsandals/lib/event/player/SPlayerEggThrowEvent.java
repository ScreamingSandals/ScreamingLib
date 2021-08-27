package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerEggThrowEvent extends SPlayerEvent {
    private final ImmutableObjectLink<EntityBasic> egg;
    private final ObjectLink<Boolean> hatching;
    private final ObjectLink<EntityTypeHolder> hatchType;
    private final ObjectLink<Byte> numHatches;

    public SPlayerEggThrowEvent(ImmutableObjectLink<PlayerWrapper> player,
                                ImmutableObjectLink<EntityBasic> egg,
                                ObjectLink<Boolean> hatching,
                                ObjectLink<EntityTypeHolder> hatchType,
                                ObjectLink<Byte> numHatches) {
        super(player);
        this.egg = egg;
        this.hatching = hatching;
        this.hatchType = hatchType;
        this.numHatches = numHatches;
    }

    public EntityBasic getEgg() {
        return egg.get();
    }

    public boolean isHatching() {
        return hatching.get();
    }

    public void setHatching(boolean hatching) {
        this.hatching.set(hatching);
    }

    public EntityTypeHolder getHatchType() {
        return hatchType.get();
    }

    public void setHatchType(EntityTypeHolder hatchType) {
        this.hatchType.set(hatchType);
    }

    public byte getNumHatches() {
        return numHatches.get();
    }

    public void setNumHatches(byte numHatches) {
        this.numHatches.set(numHatches);
    }
}
