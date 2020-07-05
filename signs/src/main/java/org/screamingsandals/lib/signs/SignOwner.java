package org.screamingsandals.lib.signs;

import java.util.List;

import org.bukkit.entity.Player;

public interface SignOwner {
	boolean isNameExists(String name);
	
	void updateSign(SignBlock sign);
	
	List<String> getSignPrefixes();
	
	void onClick(Player player, SignBlock sign);
	
	String getSignCreationPermission();
	
	String returnTranslate(String key);

	void runLater(Runnable runnable, long delay);
}
