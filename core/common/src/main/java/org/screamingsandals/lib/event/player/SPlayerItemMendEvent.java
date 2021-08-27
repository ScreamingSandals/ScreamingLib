package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityExperience;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerItemMendEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<Item> item;
    private final ImmutableObjectLink<EntityExperience> experienceOrb;
    private final ObjectLink<Integer> repairAmount;

    public SPlayerItemMendEvent(ImmutableObjectLink<PlayerWrapper> player,
                                ImmutableObjectLink<Item> item,
                                ImmutableObjectLink<EntityExperience> experienceOrb,
                                ObjectLink<Integer> repairAmount) {
        super(player);
        this.item = item;
        this.experienceOrb = experienceOrb;
        this.repairAmount = repairAmount;
    }

    public Item getItem() {
        return item.get();
    }

    public EntityExperience getExperienceOrb() {
        return experienceOrb.get();
    }

    public int getRepairAmount() {
        return repairAmount.get();
    }

    public void setRepairAmount(int repairAmount) {
        this.repairAmount.set(repairAmount);
    }
}
