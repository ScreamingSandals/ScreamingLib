package org.screamingsandals.lib.entity.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SFoodLevelChangeEvent extends CancellableAbstractEvent {
    private final EntityBasic entity;
    private int level;
    private final Item item;
}
