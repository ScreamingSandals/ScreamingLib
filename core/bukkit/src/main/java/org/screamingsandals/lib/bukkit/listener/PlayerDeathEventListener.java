package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerDeathEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

public class PlayerDeathEventListener extends AbstractBukkitEventHandlerFactory<PlayerDeathEvent, SPlayerDeathEvent> {

    public PlayerDeathEventListener(Plugin plugin) {
        super(PlayerDeathEvent.class, SPlayerDeathEvent.class, plugin);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected SPlayerDeathEvent wrapEvent(PlayerDeathEvent event, EventPriority priority) {
        var killer = event.getEntity().getKiller();

        return new SPlayerDeathEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getEntity().getPlayer())),
                ComponentObjectLink.of(event, "deathMessage", event::getDeathMessage, event::setDeathMessage),
                new CollectionLinkedToCollection<>(
                        event.getDrops(),
                        o -> o.as(ItemStack.class),
                        itemStack -> ItemFactory.build(itemStack).orElseThrow()
                ),
                ObjectLink.of(event::getKeepInventory, event::setKeepInventory),
                ObjectLink.of(event::shouldDropExperience, event::setShouldDropExperience),
                ObjectLink.of(event::getKeepLevel, event::setKeepLevel),
                ObjectLink.of(event::getNewLevel, event::setNewLevel),
                ObjectLink.of(event::getNewTotalExp, event::setNewTotalExp),
                ObjectLink.of(event::getNewExp, event::setNewExp),
                ObjectLink.of(event::getDroppedExp, event::setDroppedExp),
                ImmutableObjectLink.of(() -> killer != null ? PlayerMapper.wrapPlayer(killer) : null)
        );
    }
}
