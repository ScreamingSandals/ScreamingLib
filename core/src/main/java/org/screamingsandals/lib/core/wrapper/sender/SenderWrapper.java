package org.screamingsandals.lib.core.wrapper.sender;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

public interface SenderWrapper<T> {

    static SenderWrapper<org.bukkit.command.CommandSender> of(org.bukkit.command.CommandSender commandSender) {
        return BukkitWrapper.of(commandSender);
    }

    static SenderWrapper<net.md_5.bungee.api.CommandSender> of(net.md_5.bungee.api.CommandSender commandSender) {
        return BungeeWrapper.of(commandSender);
    }

    static SenderWrapper<CommandSource> of(CommandSource commandSource) {
        return new AbstractSender<>(commandSource, commandSource) {
        };
    }

    /**
     * Mostly a console sender.
     *
     * @return Instance of this sender.
     */
    T getInstance();

    /**
     * If the instance is console, returns just "CONSOLE" :)
     *
     * @return Name of the instance.
     */
    String getName();

    /**
     * Audience of this sender.
     *
     * @return an {@link Audience}
     */
    Audience getAudience();

    /**
     * Sends message with {@link Identity}.nil()
     * If the instance is Player, the identity is set from {@link java.util.UUID}
     *
     * @param message message to send
     */
    void sendMessage(Component message);

    /**
     * @param identity identity to use
     * @param message  message to send
     */
    void sendMessage(Identity identity, Component message);

    boolean hasPermission(String permission);

    /**
     * @return Player wrapper of this sender.
     */
    @SuppressWarnings("unchecked")
    default <K> PlayerWrapper<K> getPlayer() {
        if (getInstance().getClass().isAssignableFrom(PlayerWrapper.class)) {
            return (PlayerWrapper<K>) getInstance();
        }

        throw new UnsupportedOperationException("Cannot get player, instance is " + getInstance().getClass().getSimpleName());
    }

    /**
     * @return true if this instance is Player
     */
    default boolean isPlayer() {
        return (this instanceof BukkitWrapper.WrapperPlayer)
                || (this instanceof BungeeWrapper.WrapperPlayer);
    }
}
