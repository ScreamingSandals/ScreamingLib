package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.world.LocationHolder;

import javax.swing.text.html.parser.Entity;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SItemDespawnEvent extends CancellableAbstractEvent {
    private final EntityBasic entity;
    private final LocationHolder location;
}
