package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerInteractEntityEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<EntityBasic> clickedEntity;
    private final ImmutableObjectLink<EquipmentSlotHolder> hand;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public EntityBasic getClickedEntity() {
        return clickedEntity.get();
    }

    public EquipmentSlotHolder getHand() {
        return hand.get();
    }
}
