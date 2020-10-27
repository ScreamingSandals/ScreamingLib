package org.screamingsandals.lib.core.wrapper.sender;

import com.google.inject.Inject;
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
        return new AbstractSender<>(instance, audiences.sender(instance)) {
        };
    }

    static class WrapperPlayer extends AbstractSender<Player> implements PlayerWrapper<Player> {

        public WrapperPlayer(Player instance, Audience audience) {
            super(instance, audience);
        }

        @Override
        public UUID getUuid() {
            return instance.getUniqueId();
        }

        @Override
        public void sendMessage(Component message) {
            sendMessage(Identity.identity(getUuid()), message);
        }

        @Override
        public boolean hasPermission(String permission) {
            return instance.hasPermission(permission);
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
}
