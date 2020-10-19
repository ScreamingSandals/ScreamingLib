package org.screamingsandals.lib.core.player;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created for hoz.network, ported to ScreamingSandals
 * @param <T> Type of Player
 */
public interface PlayerWrapper<T> {

    T getInstance();

    String getName();

    UUID getUuid();

    String getAddress();

    void kick(TextComponent reason);

    void sendMessage(TextComponent message);

    static PlayerWrapper<Player> of(Player instance) {
        if (instance != null) {
            return new PaperPlayerWrapper(instance);
        }
        return null;
    }

    static PlayerWrapper<ProxiedPlayer> of(ProxiedPlayer instance) {
        if (instance != null) {
            return new BungeePlayerWrapper(instance);
        }
        return null;
    }
}
