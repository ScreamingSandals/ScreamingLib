package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerCommandPreprocessEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerCommandPreprocessEvent implements SPlayerCommandPreprocessEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerCommandPreprocessEvent event;

    @Override
    public PlayerWrapper getPlayer() {
        return new BukkitEntityPlayer(event.getPlayer());
    }
    @Override
    public void setPlayer(PlayerWrapper player) {
        event.setPlayer(player.as(Player.class));
    }

    @Override
    public String getCommand() {
        return event.getMessage();
    }

    @Override
    public void setCommand(String command) {
        event.setMessage(command);
    }
}
