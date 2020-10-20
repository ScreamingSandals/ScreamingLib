package org.screamingsandals.lib.core.wrapper;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created for hoz.network, ported to ScreamingSandals
 *
 * @param <T> Type of Player
 */
public interface PlayerWrapper<T> extends SenderWrapper<T> {

    UUID getUuid();

    String getAddress();

    void kick(TextComponent reason);

    void kick(BaseComponent[] reason);

    static PlayerWrapper<Player> of(Player instance) {
        Preconditions.checkNotNull(instance, "instance");
        return new PaperPlayerWrapper(instance);
    }

    static PlayerWrapper<ProxiedPlayer> of(ProxiedPlayer instance) {
        Preconditions.checkNotNull(instance, "instance");
        return new BungeePlayerWrapper(instance);
    }
}
