package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockFormEvent;
import org.screamingsandals.lib.event.block.SBlockFormedByEntityEvent;
import org.screamingsandals.lib.event.block.SBlockGrowEvent;
import org.screamingsandals.lib.event.block.SBlockSpreadEvent;
import org.screamingsandals.lib.block.state.BlockStateMapper;

public class BlockGrowEventListener extends AbstractBukkitEventHandlerFactory<BlockGrowEvent, SBlockGrowEvent> {

    public BlockGrowEventListener(Plugin plugin) {
        super(BlockGrowEvent.class, SBlockGrowEvent.class, plugin);
    }

    @Override
    protected SBlockGrowEvent wrapEvent(BlockGrowEvent event, EventPriority priority) {
        if (event instanceof BlockFormEvent) {
            if (event instanceof EntityBlockFormEvent) {
                return new SBlockFormedByEntityEvent(
                        ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                        ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(((EntityBlockFormEvent) event).getEntity()).orElseThrow()),
                        ImmutableObjectLink.of(() -> BlockStateMapper.wrapBlockState(event.getNewState()).orElseThrow())
                );
            }

            if (event instanceof BlockSpreadEvent) {
                return new SBlockSpreadEvent(
                        ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                        ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(((BlockSpreadEvent) event).getSource())),
                        ImmutableObjectLink.of(() -> BlockStateMapper.wrapBlockState(event.getNewState()).orElseThrow())
                );
            }

            return new SBlockFormEvent(
                    ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                    ImmutableObjectLink.of(() -> BlockStateMapper.wrapBlockState(event.getNewState()).orElseThrow())
            );
        }

        return new SBlockGrowEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> BlockStateMapper.wrapBlockState(event.getNewState()).orElseThrow())
        );
    }
}
