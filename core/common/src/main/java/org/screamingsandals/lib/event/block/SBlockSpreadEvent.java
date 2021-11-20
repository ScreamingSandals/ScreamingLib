package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockSpreadEvent extends SBlockFormEvent {
    BlockHolder getSource();
}
