package org.screamingsandals.lib.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacket;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.reflect.InvocationResult;
import org.screamingsandals.lib.utils.reflect.Reflect;

public abstract class BukkitSPacket extends SPacket {
    protected final InvocationResult packet;

    public BukkitSPacket(Class<?> packetClass) {
        packet = Reflect.constructResulted(packetClass);
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

        boolean result = ClassStorage.sendPacket((Player) player.getWrappedPlayer().get(), packet.raw());
        if (!result) {
            Bukkit.getLogger().warning("Could not send packet: " + this.getClass().getSimpleName() + " to player: " + player.getName());
        }
    }
}
