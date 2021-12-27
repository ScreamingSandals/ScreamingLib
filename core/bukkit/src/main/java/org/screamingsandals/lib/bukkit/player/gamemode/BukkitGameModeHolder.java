package org.screamingsandals.lib.bukkit.player.gamemode;

import org.bukkit.GameMode;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitGameModeHolder extends BasicWrapper<GameMode> implements GameModeHolder {
    protected BukkitGameModeHolder(GameMode wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public int id() {
        return wrappedObject.getValue();
    }

    @Override
    public boolean is(Object gameMode) {
        if (gameMode instanceof GameMode || gameMode instanceof GameModeHolder) {
            return equals(gameMode);
        }
        return equals(GameModeHolder.ofOptional(gameMode).orElse(null));
    }

    @Override
    public boolean is(Object... gameModes) {
        return Arrays.stream(gameModes).anyMatch(this::is);
    }

}
