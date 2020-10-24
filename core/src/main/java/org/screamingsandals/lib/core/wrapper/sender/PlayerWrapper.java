package org.screamingsandals.lib.core.wrapper.sender;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
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

    void kick(Component reason);

    static PlayerWrapper<Player> of(Player instance) {
        Preconditions.checkNotNull(instance, "instance");
        return BukkitWrapper.of(instance);
    }

    static PlayerWrapper<ProxiedPlayer> of(ProxiedPlayer instance) {
        Preconditions.checkNotNull(instance, "instance");
        return BungeeWrapper.of(instance);
    }
}
