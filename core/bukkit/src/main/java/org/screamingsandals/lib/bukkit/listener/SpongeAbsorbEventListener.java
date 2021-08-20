package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.block.BlockState;
import org.bukkit.event.block.SpongeAbsorbEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.world.SSpongeAbsorbEvent;
import org.screamingsandals.lib.world.state.BlockStateMapper;

public class SpongeAbsorbEventListener extends AbstractBukkitEventHandlerFactory<SpongeAbsorbEvent, SSpongeAbsorbEvent> {

    public SpongeAbsorbEventListener(Plugin plugin) {
        super(SpongeAbsorbEvent.class, SSpongeAbsorbEvent.class, plugin);
    }

    @Override
    protected SSpongeAbsorbEvent wrapEvent(SpongeAbsorbEvent event, EventPriority priority) {
        return new SSpongeAbsorbEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                new CollectionLinkedToCollection<>(event.getBlocks(), o -> o.as(BlockState.class), o -> BlockStateMapper.wrapBlockState(o).orElseThrow())
        );
    }
}
