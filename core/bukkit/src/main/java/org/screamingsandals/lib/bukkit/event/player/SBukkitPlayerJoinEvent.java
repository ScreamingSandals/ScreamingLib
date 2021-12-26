package org.screamingsandals.lib.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.event.player.SPlayerJoinEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.SenderMessage;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerJoinEvent implements SPlayerJoinEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerJoinEvent event;

    // Internal cache
    private PlayerWrapper player;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    @Nullable
    public Component getJoinMessage() {
        return ComponentObjectLink.processGetter(event, "joinMessage", event::getJoinMessage);
    }

    @Override
    public void setJoinMessage(@Nullable Component joinMessage) {
        ComponentObjectLink.processSetter(event, "joinMessage", event::setJoinMessage, joinMessage);
    }

    @Override
    public void setJoinMessage(@Nullable ComponentLike joinMessage) {
        if (joinMessage instanceof SenderMessage) {
            setJoinMessage(((SenderMessage) joinMessage).asComponent(getPlayer()));
        } else {
            setJoinMessage(joinMessage != null ? joinMessage.asComponent() : null);
        }
    }
}