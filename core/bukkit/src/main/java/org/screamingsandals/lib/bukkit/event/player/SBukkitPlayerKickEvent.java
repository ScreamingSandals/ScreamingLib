package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.event.player.PlayerKickEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerKickEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.sender.SenderMessage;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

@Data
public class SBukkitPlayerKickEvent implements SPlayerKickEvent, BukkitCancellable {
    private final PlayerKickEvent event;

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
    public Component getLeaveMessage() {
        return ComponentObjectLink.processGetter(event, "leaveMessage", event::getLeaveMessage);
    }

    @Override
    public void setLeaveMessage(Component leaveMessage) {
        ComponentObjectLink.processSetter(event, "leaveMessage", event::setReason, leaveMessage);

    }

    @Override
    public void setLeaveMessage(ComponentLike leaveMessage) {
        if (leaveMessage instanceof SenderMessage) {
            setLeaveMessage(((SenderMessage) leaveMessage).asComponent(getPlayer()));
        } else {
            setLeaveMessage(leaveMessage.asComponent());
        }
    }

    @Override
    public Component getKickReason() {
        return ComponentObjectLink.processGetter(event, "reason", event::getReason);
    }

    @Override
    public void setKickReason(Component kickReason) {
        ComponentObjectLink.processSetter(event, "reason", event::setReason, kickReason);
    }

    @Override
    public void setKickReason(ComponentLike kickReason) {
        if (kickReason instanceof SenderMessage) {
            setKickReason(((SenderMessage) kickReason).asComponent(getPlayer()));
        } else {
            setKickReason(kickReason.asComponent());
        }
    }
}
