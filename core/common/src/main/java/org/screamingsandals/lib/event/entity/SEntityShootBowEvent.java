package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityShootBowEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<Item> bow;
    private final ImmutableObjectLink<Item> consumable;
    private final ImmutableObjectLink<EntityBasic> projectile;
    private final ImmutableObjectLink<EquipmentSlotHolder> hand;
    private final ImmutableObjectLink<Float> force;
    private final ObjectLink<Boolean> consumeItem;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public Item getBow() {
        return bow.get();
    }

    public Item getConsumable() {
        return consumable.get();
    }

    public EntityBasic getProjectile() {
        return projectile.get();
    }

    public EquipmentSlotHolder getHand() {
        return hand.get();
    }

    public float getForce() {
        return force.get();
    }

    public boolean isConsumeItem() {
        return consumeItem.get();
    }

    public void setConsumeItem(boolean consumeItem) {
        this.consumeItem.set(consumeItem);
    }
}
