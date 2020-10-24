package org.screamingsandals.lib.core.wrapper.sender;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;

import java.util.UUID;

public class BukkitWrapper {
    private static BukkitAudiences audiences;

    @Inject
    public BukkitWrapper(PluginWrapper pluginWrapper) {
        audiences = BukkitAudiences.create(pluginWrapper.getPlugin());
    }

    static PlayerWrapper<Player> of(Player instance) {
        return new WrapperPlayer(instance, audiences.player(instance));
    }

    static SenderWrapper<CommandSender> of(CommandSender instance) {
        return new Sender<>(instance, audiences.sender(instance)) {
        };
    }

    static class WrapperPlayer extends Sender<Player> implements PlayerWrapper<Player> {

        public WrapperPlayer(Player instance, Audience audience) {
            super(instance, audience);
        }

        @Override
        public UUID getUuid() {
            return instance.getUniqueId();
        }

        @Override
        public void sendMessage(Component message) {
            audience.sendMessage(Identity.identity(getUuid()), message);
        }

        @Override
        public String getAddress() {
            final var address = instance.getAddress();
            if (address == null) {
                return "UNKNOWN";
            }

            return address.getAddress().getHostAddress();
        }

        @Override
        public void kick(Component reason) {
            instance.kickPlayer(LegacyComponentSerializer.legacyAmpersand().serialize(reason));
        }
    }

    @RequiredArgsConstructor
    @Getter
    static abstract class Sender<T> implements SenderWrapper<T> {
        protected final T instance;
        protected final Audience audience;

        @Override
        public String getName() {
            return "CONSOLE";
        }

        @Override
        public void sendMessage(Component message) {
            audience.sendMessage(message);
        }

        @Override
        public void sendMessage(Identity identity, Component message) {
            audience.sendMessage(identity, message);
        }

        @Override
        public Audience getAudience() {
            return audience;
        }
    }
}
