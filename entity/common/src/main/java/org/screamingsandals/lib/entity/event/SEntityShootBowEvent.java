package org.screamingsandals.lib.entity.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityShootBowEvent extends CancellableAbstractEvent {
    private final EntityBasic entity;
    private final Item bow;
    private final Item consumable;
    private final EntityBasic projectile;
    private final EquipmentSlotHolder hand;
    private final float force;
    private boolean consumeItem;
}
