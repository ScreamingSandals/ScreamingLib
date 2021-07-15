package org.screamingsandals.lib.bukkit.packet;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacket;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.reflect.InvocationResult;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BukkitSPacket implements SPacket {
    protected InvocationResult packet;
    private final List<Object> additionalPackets = new ArrayList<>();

    public BukkitSPacket(Class<?> packetClass) {
        packet = new InvocationResult(Reflect.forceConstruct(packetClass));
    }

    public BukkitSPacket() {}

    public void addAdditionalPacket(InvocationResult packet) {
        addAdditionalPacket(packet.raw());
    }

    public void addAdditionalPacket(Object packet) {
        additionalPackets.add(packet);
    }

    @Override
    public void sendPacket(PlayerWrapper player) {
        if (player == null) {
            throw new UnsupportedOperationException("Player cannot be null!");
        }

        final var bukkitPlayer = (Player) player.getWrappedPlayer().get();

        if (bukkitPlayer == null) {
            log.warn("Cannot send packet to null player!");
        }

        if (!player.isOnline()) {
            log.trace("Cannot send packet to offline player!");
            return;
        }

        if (packet == null || packet.raw() == null) {
            log.warn("Packet is null!");
        }

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
