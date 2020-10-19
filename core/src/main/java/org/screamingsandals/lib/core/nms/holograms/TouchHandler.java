package org.screamingsandals.lib.core.nms.holograms;

import org.bukkit.entity.Player;

public interface TouchHandler {
	void handle(Player player, Hologram hologram);
}
