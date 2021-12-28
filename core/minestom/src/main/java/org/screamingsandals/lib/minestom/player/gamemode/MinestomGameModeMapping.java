package org.screamingsandals.lib.minestom.player.gamemode;

import net.minestom.server.entity.GameMode;
import org.screamingsandals.lib.player.gamemode.GameModeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class MinestomGameModeMapping extends GameModeMapping {
    public MinestomGameModeMapping() {
        gameModeConverter
                .registerP2W(GameMode.class, MinestomGameModeHolder::new)
                .registerW2P(GameMode.class, gameModeHolder -> GameMode.valueOf(gameModeHolder.platformName()));

        Arrays.stream(GameMode.values()).forEach(gameMode -> {
            var holder = new MinestomGameModeHolder(gameMode);
            mapping.put(NamespacedMappingKey.of(gameMode.name()), holder);
            values.add(holder);
        });
    }
}
