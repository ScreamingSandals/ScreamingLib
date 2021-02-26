package org.screamingsandals.lib.player.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerRightClickedEntityEvent extends CancellableAbstractEvent {
    private PlayerWrapper player;
    private PlayerWrapper.Hand hand;
    private EntityBasic entity;
}
