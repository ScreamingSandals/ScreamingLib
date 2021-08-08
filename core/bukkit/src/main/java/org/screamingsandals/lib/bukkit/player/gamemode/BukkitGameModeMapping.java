package org.screamingsandals.lib.bukkit.player.gamemode;

import org.bukkit.GameMode;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.player.gamemode.GameModeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitGameModeMapping extends GameModeMapping {
    public BukkitGameModeMapping() {
        gameModeConverter
                .registerP2W(GameMode.class, gameMode -> new GameModeHolder(gameMode.name()))
                .registerW2P(GameMode.class, gameModeHolder -> GameMode.valueOf(gameModeHolder.getPlatformName()));

        Arrays.stream(GameMode.values()).forEach(gameMode -> mapping.put(NamespacedMappingKey.of(gameMode.name()), new GameModeHolder(gameMode.name())));
    }

    @Override
    protected int getId0(GameModeHolder holder) {
        return holder.as(GameMode.class).getValue();
    }
}
