package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import org.bukkit.event.block.BlockShearEntityEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.block.SBlockShearEntityEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;

@Data
public class SBukkitBlockShearEntityEvent implements SBlockShearEntityEvent, BukkitCancellable {
    private final BlockShearEntityEvent event;

    // Internal cache
    private BlockHolder block;
    private EntityBasic entity;
    private Item tool;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public Item getTool() {
        if (tool == null) {
            tool = ItemFactory.build(event.getTool()).orElseThrow();
        }
        return tool;
    }
}
