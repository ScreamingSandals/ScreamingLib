package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityExperience;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerItemMendEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<Item> item;
    private final ImmutableObjectLink<EntityExperience> experienceOrb;
    private final ObjectLink<Integer> repairAmount;

    public PlayerWrapper getPlayer() {
        return player.get();
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
