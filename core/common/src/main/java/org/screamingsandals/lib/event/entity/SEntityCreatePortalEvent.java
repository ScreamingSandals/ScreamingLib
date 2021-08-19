package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.PortalType;
import org.screamingsandals.lib.world.state.BlockStateHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityCreatePortalEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final Collection<BlockStateHolder> blocks;
    private final ImmutableObjectLink<PortalType> portalType;
}
