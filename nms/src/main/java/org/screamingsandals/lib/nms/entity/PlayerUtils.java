package org.screamingsandals.lib.nms.entity;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static org.screamingsandals.lib.nms.utils.ClassStorage.NMS.*;
import static org.screamingsandals.lib.reflection.Reflection.*;
import static org.screamingsandals.lib.nms.utils.ClassStorage.*;

public class PlayerUtils {
	public static void respawn(Plugin instance, Player player, long delay) {
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					player.spigot().respawn();
				} catch (Throwable t) {
					try {
						var selectedObj = findEnumConstant(EnumClientCommand, "PERFORM_RESPAWN");
						var packet = PacketPlayInClientCommand.getDeclaredConstructor(EnumClientCommand)
							.newInstance(selectedObj);
						var connection = getPlayerConnection(player);
						getMethod(connection, "a,func_147342_a", PacketPlayInClientCommand).invoke(packet);
					} catch (Throwable ignored) {
						t.printStackTrace();
					}
				}
			}
		}.runTaskLater(instance, delay);
	}

	public static void fakeExp(Player player, float percentage, int levels) {
		try {
			var packet = PacketPlayOutExperience.getConstructor(float.class, int.class, int.class)
				.newInstance(percentage, player.getTotalExperience(), levels);
			sendPacket(player, packet);
		} catch (Throwable ignored) {
		}
	}
}
