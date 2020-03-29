package org.screamingsandals.lib.signs;

import java.util.List;

import org.bukkit.entity.Player;

public interface SignOwner {
	public boolean isNameExists(String name);
	
	public void updateSign(SignBlock sign);
	
	public List<String> getSignPrefixes();
	
	public void onClick(Player player, SignBlock sign);
	
	public String getSignCreationPermission();
	
	public String returnTranslate(String key);
}
