package org.screamingsandals.lib.core.nms.network.outbound;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public abstract class AutoPacketOutboundListener extends PacketOutboundListener implements Listener {
	public AutoPacketOutboundListener(Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getOnlinePlayers().forEach(this::addPlayer);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public final void onPlayerLogin(PlayerJoinEvent e) {
		addPlayer(e.getPlayer());
	}

	@EventHandler
	public final void onPluginDisable(PluginDisableEvent e) {
		Bukkit.getOnlinePlayers().forEach(this::removePlayer);
	}
}
