package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerItemConsumeEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class PlayerItemConsumeEventListener extends AbstractBukkitEventHandlerFactory<PlayerItemConsumeEvent, SPlayerItemConsumeEvent> {

    public PlayerItemConsumeEventListener(Plugin plugin) {
        super(PlayerItemConsumeEvent.class, SPlayerItemConsumeEvent.class, plugin);
    }

    @Override
    protected SPlayerItemConsumeEvent wrapEvent(PlayerItemConsumeEvent event, EventPriority priority) {
        return new SPlayerItemConsumeEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ObjectLink.of(
                        () -> ItemFactory.build(event.getItem()).orElse(null),
                        item -> event.setItem(item != null ? item.as(ItemStack.class) : null)
                )
        );
    }
}
