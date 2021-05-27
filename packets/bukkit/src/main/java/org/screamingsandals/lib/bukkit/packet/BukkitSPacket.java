package org.screamingsandals.lib.bukkit.packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacket;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.reflect.InvocationResult;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
import java.util.List;

public abstract class BukkitSPacket implements SPacket {
    protected InvocationResult packet;
    private final List<Object> additionalPackets = new ArrayList<>();

    public BukkitSPacket(Class<?> packetClass) {
        packet = Reflect.constructResulted(packetClass);
    }

    public void addAdditionalPacket(InvocationResult packet) {
        addAdditionalPacket(packet.raw());
    }

    public void addAdditionalPacket(Object packet) {
        additionalPackets.add(packet);
    }

    @Override
    public void sendPacket(PlayerWrapper player) {
        if (player == null || player.getWrappedPlayer().get() == null) {
            throw new UnsupportedOperationException("Player cannot be null!");
        }

        if (!player.isOnline()) {
            throw new UnsupportedOperationException("Cannot send packet to offline player!");
        }

        if (packet == null) {
            throw new UnsupportedOperationException("Packet cannot be null!");
        }

        final var bukkitPlayer = (Player) player.getWrappedPlayer().get();
        boolean result = ClassStorage.sendPacket(bukkitPlayer, packet.raw());
        if (!result) {
            Bukkit.getLogger().warning("Could not send packet: " + this.getClass().getSimpleName() + " to player: " + player.getName());
        }
        additionalPackets
                .forEach(packet -> ClassStorage.sendPacket(bukkitPlayer, packet));
    }

    @Override
    public void sendPacket(List<PlayerWrapper> players) {
        players.forEach(this::sendPacket);
    }

    @Override
    public Object getRawPacket() {
        return packet.raw();
    }
}
