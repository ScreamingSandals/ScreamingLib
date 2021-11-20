package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.block.SCauldronLevelChangeEvent;

@Data
public class SBukkitCauldronLevelChangeEvent implements SCauldronLevelChangeEvent, BukkitCancellable {
    private final CauldronLevelChangeEvent event;

    // Internal cache
    private BlockHolder block;
    private EntityBasic entity;
    private boolean entityConverted;
    private Reason reason;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    @Nullable
    public EntityBasic getEntity() {
        if (!entityConverted) {
            if (event.getEntity() != null) {
                entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
            }
            entityConverted = true;
        }
        return entity;
    }

    @Override
    public int getOldLevel() {
        return event.getOldLevel();
    }

    @Override
    public Reason getReason() {
        if (reason == null) {
            reason = Reason.get(event.getReason().name());
        }
        return reason;
    }

    @Override
    public int getNewLevel() {
        return event.getNewLevel();
    }

    @Override
    public void setNewLevel(int newLevel) {
        event.setNewLevel(newLevel);
    }
}
