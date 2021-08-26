package org.screamingsandals.lib.bukkit.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerUpdateSignEvent;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockMapper;

import java.util.Arrays;

public class PlayerSignChangeEventListener extends AbstractBukkitEventHandlerFactory<SignChangeEvent, SPlayerUpdateSignEvent> {

    public PlayerSignChangeEventListener(Plugin plugin) {
        super(SignChangeEvent.class, SPlayerUpdateSignEvent.class, plugin);
    }

    @Override
    protected SPlayerUpdateSignEvent wrapEvent(SignChangeEvent event, EventPriority priority) {
        return new SPlayerUpdateSignEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                Arrays.stream(event.getLines()).map(AdventureHelper::toComponent).toArray(Component[]::new),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock()))
        );
    }

    @Override
    protected void postProcess(SPlayerUpdateSignEvent wrappedEvent, SignChangeEvent event) {
        for (int i = 0; i < event.getLines().length; i++) {
            event.setLine(i, AdventureHelper.toLegacy(wrappedEvent.getLines()[i]));
        }
    }
}
