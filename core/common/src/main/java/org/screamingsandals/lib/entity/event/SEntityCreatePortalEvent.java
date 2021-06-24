package org.screamingsandals.lib.entity.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.PortalType;
import org.screamingsandals.lib.world.state.BlockStateHolder;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityCreatePortalEvent extends CancellableAbstractEvent {
    private final EntityBasic entity;
    private final List<BlockStateHolder> blocks;
    private final PortalType portalType;
}
