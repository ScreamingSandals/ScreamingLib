package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityBreedEvent extends CancellableAbstractEvent {
    private final EntityBasic entity;
    private final EntityBasic mother;
    private final EntityBasic father;
    private final EntityBasic breeder;
    private final Item bredWith;
    private int experience;
}
