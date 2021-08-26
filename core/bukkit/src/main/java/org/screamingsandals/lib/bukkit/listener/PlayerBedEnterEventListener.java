package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerBedEnterEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockMapper;

public class PlayerBedEnterEventListener extends AbstractBukkitEventHandlerFactory<PlayerBedEnterEvent, SPlayerBedEnterEvent> {

    public PlayerBedEnterEventListener(Plugin plugin) {
        super(PlayerBedEnterEvent.class, SPlayerBedEnterEvent.class, plugin);
    }

    @Override
    protected SPlayerBedEnterEvent wrapEvent(PlayerBedEnterEvent event, EventPriority priority) {
        return new SPlayerBedEnterEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBed())),
                ImmutableObjectLink.of(() -> SPlayerBedEnterEvent.BedEnterResult.convert(event.getBedEnterResult().name())),
                ObjectLink.of(() -> AbstractEvent.Result.valueOf(event.useBed().name().toUpperCase()), result -> Event.Result.valueOf(result.name().toUpperCase()))
        );
    }
}
