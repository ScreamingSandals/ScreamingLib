package org.screamingsandals.lib.bukkit.player.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerDeathEvent;
import org.screamingsandals.lib.utils.AdventureHelper;

import java.util.stream.Collectors;

public class PlayerDeathEventListener extends AbstractBukkitEventHandlerFactory<PlayerDeathEvent, SPlayerDeathEvent> {

    public PlayerDeathEventListener(Plugin plugin) {
        super(PlayerDeathEvent.class, SPlayerDeathEvent.class, plugin);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected SPlayerDeathEvent wrapEvent(PlayerDeathEvent event, EventPriority priority) {
        Component deathMessage;
        try {
            //TODO: Solve cast issue with Papers adventure components, and our shaded adventure components
            // deathMessage = event.deathMessage();
            throw new Exception();
        } catch (Exception e) {
            deathMessage = AdventureHelper.toComponent(
                    event.getDeathMessage() == null ? "" :
                            event.getDeathMessage()
            );
        }
        return new SPlayerDeathEvent(
                PlayerMapper.wrapPlayer(event.getEntity().getPlayer()),
                deathMessage,
                event.getDrops()
                        .stream()
                        .map(itemStack -> ItemFactory.build(itemStack).orElseThrow())
                        .collect(Collectors.toList()),
                event.getKeepInventory(),
                event.shouldDropExperience(),
                event.getKeepLevel(),
                event.getNewLevel(),
                event.getNewTotalExp(),
                event.getNewExp());
    }

    @Override
    protected void postProcess(SPlayerDeathEvent wrappedEvent, PlayerDeathEvent event) {
        event.setKeepInventory(wrappedEvent.isKeepInventory());
        event.setKeepLevel(wrappedEvent.isKeepLevel());
        event.setNewExp(wrappedEvent.getNewExp());
        event.setNewTotalExp(wrappedEvent.getNewTotalExp());
        event.setNewLevel(wrappedEvent.getNewLevel());
        event.setShouldDropExperience(wrappedEvent.isShouldDropExperience());
        event.getDrops().clear();

        wrappedEvent.getDrops()
                .stream()
                .map(item -> item.as(ItemStack.class))
                .forEach(itemStack -> event.getDrops().add(itemStack));
    }
}
