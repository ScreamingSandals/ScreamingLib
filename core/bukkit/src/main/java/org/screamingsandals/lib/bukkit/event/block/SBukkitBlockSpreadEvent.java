package org.screamingsandals.lib.bukkit.event.block;

import org.bukkit.event.block.BlockSpreadEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockSpreadEvent;

public class SBukkitBlockSpreadEvent extends SBukkitBlockFormEvent implements SBlockSpreadEvent {
    // Internal cache
    private BlockHolder source;

    public SBukkitBlockSpreadEvent(BlockSpreadEvent event) {
        super(event);
    }

    @Override
    public BlockHolder getSource() {
        if (source == null) {
            source = BlockMapper.wrapBlock(((BlockSpreadEvent) getEvent()).getSource());
        }
        return source;
    }
}
