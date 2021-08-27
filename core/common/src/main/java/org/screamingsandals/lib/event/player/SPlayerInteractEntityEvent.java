package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerInteractEntityEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<EntityBasic> clickedEntity;
    private final ImmutableObjectLink<EquipmentSlotHolder> hand;

    public SPlayerInteractEntityEvent(ImmutableObjectLink<PlayerWrapper> player,
                                      ImmutableObjectLink<EntityBasic> clickedEntity,
                                      ImmutableObjectLink<EquipmentSlotHolder> hand) {
        super(player);
        this.clickedEntity = clickedEntity;
        this.hand = hand;
    }

    public EntityBasic getClickedEntity() {
        return clickedEntity.get();
    }

    public EquipmentSlotHolder getHand() {
        return hand.get();
    }
}
