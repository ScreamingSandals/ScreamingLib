package org.screamingsandals.lib.bukkit.listener;
;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.BlockDataMapper;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SFluidLevelChangeEvent;

public class FluidLevelChangeEventListener extends AbstractBukkitEventHandlerFactory<FluidLevelChangeEvent, SFluidLevelChangeEvent> {

    public FluidLevelChangeEventListener(Plugin plugin) {
        super(FluidLevelChangeEvent.class, SFluidLevelChangeEvent.class, plugin);
    }

    @Override
    protected SFluidLevelChangeEvent wrapEvent(FluidLevelChangeEvent event, EventPriority priority) {
        return new SFluidLevelChangeEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                BlockDataMapper.resolve(event.getNewData()).orElseThrow()
        );
    }
}
