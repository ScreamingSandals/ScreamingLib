package org.screamingsandals.lib.bukkit.event.player;

import org.bukkit.event.block.BlockBreakEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.event.block.SBukkitBlockExperienceEvent;
import org.screamingsandals.lib.event.player.SPlayerBlockBreakEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

public class SBukkitPlayerBlockBreakEvent extends SBukkitBlockExperienceEvent implements SPlayerBlockBreakEvent, BukkitCancellable {
    public SBukkitPlayerBlockBreakEvent(BlockBreakEvent event) {
        super(event);
    }

    // Internal cache
    private PlayerWrapper player;

    @Override
    public boolean isDropItems() {
        return getEvent().isDropItems();
    }

    @Override
    public void setDropItems(boolean dropItems) {
        getEvent().setDropItems(dropItems);
    }

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(getEvent().getPlayer());
        }
        return player;
    }

    @Override
    public BlockBreakEvent getEvent() {
        return (BlockBreakEvent) super.getEvent();
    }
}
