package org.screamingsandals.lib.minestom.player.gamemode;

import net.minestom.server.entity.GameMode;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class MinestomGameModeHolder extends BasicWrapper<GameMode> implements GameModeHolder {
    protected MinestomGameModeHolder(GameMode wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public int id() {
        return wrappedObject.getId();
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
