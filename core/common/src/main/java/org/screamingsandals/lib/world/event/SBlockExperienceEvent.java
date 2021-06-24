package org.screamingsandals.lib.world.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SBlockExperienceEvent extends AbstractEvent {
    private final BlockHolder block;
    private int experience;
}
