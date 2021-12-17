package org.screamingsandals.lib.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.SenderMessage;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerLeaveEvent implements SPlayerLeaveEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerQuitEvent event;

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
    public Component getLeaveMessage() {
        return ComponentObjectLink.processGetter(event, "quitMessage", event::getQuitMessage);
    }

    @Override
    public void setLeaveMessage(@Nullable Component leaveMessage) {
        ComponentObjectLink.processSetter(event, "quitMessage", event::setQuitMessage, leaveMessage);
    }

    @Override
    public void setLeaveMessage(@Nullable ComponentLike leaveMessage) {
        if (leaveMessage instanceof SenderMessage) {
            setLeaveMessage(((SenderMessage) leaveMessage).asComponent(getPlayer()));
        } else {
            setLeaveMessage(leaveMessage != null ? leaveMessage.asComponent() : null);
        }
    }
}
