package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerEggThrowEvent extends AbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<EntityBasic> egg;
    private final ObjectLink<Boolean> hatching;
    private final ObjectLink<EntityTypeHolder> hatchType;
    private final ObjectLink<Byte> numHatches;

    public PlayerWrapper getPlayer() {
        return player.get();
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
