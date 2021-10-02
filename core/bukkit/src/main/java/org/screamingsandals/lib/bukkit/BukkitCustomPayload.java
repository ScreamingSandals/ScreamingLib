package org.screamingsandals.lib.bukkit;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListenerRegistration;
import org.screamingsandals.lib.CustomPayload;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Collection;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class BukkitCustomPayload extends CustomPayload {
    private final Plugin plugin;

    @Override
    protected void registerOutgoingChannel0(String channel) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, channel);
    }

    @Override
    protected void unregisterOutgoingChannel0(String channel) {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, channel);
    }

    @Override
    protected void unregisterAllOutgoingChannels0() {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin);
    }

    @Override
    protected Collection<String> getRegisteredOutgoingChannels0() {
        return Bukkit.getMessenger().getOutgoingChannels(plugin);
    }

    @Override
    protected Registration registerIncomingChannel0(String channel, BiConsumer<PlayerWrapper, byte[]> listener) {
        return new BukkitRegistration(Bukkit.getMessenger().registerIncomingPluginChannel(plugin, channel, (channel1, player, message) -> {
            if (channel1.equals(channel)) {
                listener.accept(PlayerMapper.wrapPlayer(player), message);
            }
        }));
    }

    @Override
    protected void unregisterIncomingChannel0(Registration registration) {
        if (!(registration instanceof BukkitRegistration)) {
            throw new UnsupportedOperationException("Registration is not instance of BukkitRegistration class!");
        }
        var bRegistration = registration.as(PluginMessageListenerRegistration.class);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(bRegistration.getPlugin(), bRegistration.getChannel(), bRegistration.getListener());
    }

    @Override
    protected void unregisterAllIncomingChannels0() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin);
    }

    @Override
    protected Collection<String> getRegisteredIncomingChannels0() {
        return Bukkit.getMessenger().getIncomingChannels(plugin);
    }

    @Override
    protected void send0(PlayerWrapper player, String channel, byte[] payload) {
        player.as(Player.class).sendPluginMessage(plugin, channel, payload);
    }

    @Override
    protected void send0(String channel, byte[] payload) {
        Bukkit.getServer().sendPluginMessage(plugin, channel, payload);
    }

    public static class BukkitRegistration extends BasicWrapper<PluginMessageListenerRegistration> implements Registration {

        protected BukkitRegistration(PluginMessageListenerRegistration wrappedObject) {
            super(wrappedObject);
        }
    }
}
