package org.screamingsandals.lib.entity.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityPortalEnterEvent extends AbstractEvent {
    private final EntityBasic entity;
    private final LocationHolder location;
}
