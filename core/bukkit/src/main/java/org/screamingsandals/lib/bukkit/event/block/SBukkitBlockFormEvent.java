package org.screamingsandals.lib.bukkit.event.block;

import org.bukkit.event.block.BlockFormEvent;
import org.screamingsandals.lib.event.block.SBlockFormEvent;

public class SBukkitBlockFormEvent extends SBukkitBlockGrowEvent implements SBlockFormEvent {
    public SBukkitBlockFormEvent(BlockFormEvent event) {
        super(event);
    }
}
