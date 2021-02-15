package org.screamingsandals.lib.bukkit.utils.nms.network;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public abstract class AutoPacketInboundListener extends PacketInboundListener implements Listener {

	public AutoPacketInboundListener(Plugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		Bukkit.getOnlinePlayers().forEach(this::addPlayer);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public final void onPlayerLogin(PlayerJoinEvent e) {
		addPlayer(e.getPlayer());
	}

	@EventHandler(priority =  EventPriority.LOWEST)
	public final void onPlayerQuit(PlayerQuitEvent e) {
		removePlayer(e.getPlayer());
	}

	@EventHandler
	public final void onPluginDisable(PluginDisableEvent e) {
		Bukkit.getOnlinePlayers().forEach(this::removePlayer);
	}
}
