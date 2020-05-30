package org.screamingsandals.lib.gamecore.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.gamecore.GameCore;

public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return "sbw";
    }

    @Override
    public String getAuthor() {
        return "ScreamingSandals";
    }

    @Override
    public String getVersion() {
        return GameCore.getPlugin().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        final var gamePlayerRegistration = GameCore.getPlayerManager().getRegisteredPlayer(player);
        if (gamePlayerRegistration.isEmpty()) {
            return "Not Found!";
        }

        final var gamePlayer = gamePlayerRegistration.get();

        //TODO
        return "";
    }
}
