package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerBedEnterEvent;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBedEnterEventListener extends AbstractBukkitEventHandlerFactory<PlayerBedEnterEvent, SPlayerBedEnterEvent> {

    public PlayerBedEnterEventListener(Plugin plugin) {
        super(PlayerBedEnterEvent.class, SPlayerBedEnterEvent.class, plugin);
    }

    @Override
    protected SPlayerBedEnterEvent wrapEvent(PlayerBedEnterEvent event, EventPriority priority) {
        return new SPlayerBedEnterEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                BlockMapper.wrapBlock(event.getBed()),
                SPlayerBedEnterEvent.BedEnterResult.convert(event.getBedEnterResult().name())
        );
    }

    @Override
    protected void postProcess(SPlayerBedEnterEvent wrappedEvent, PlayerBedEnterEvent event) {
        event.setUseBed(Event.Result.valueOf(wrappedEvent.getUseBed().name().toUpperCase()));
    }
}
