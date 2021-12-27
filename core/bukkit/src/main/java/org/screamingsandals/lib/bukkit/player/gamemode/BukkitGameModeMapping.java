package org.screamingsandals.lib.bukkit.player.gamemode;

import org.bukkit.GameMode;
import org.screamingsandals.lib.player.gamemode.GameModeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitGameModeMapping extends GameModeMapping {
    public BukkitGameModeMapping() {
        gameModeConverter
                .registerP2W(GameMode.class, BukkitGameModeHolder::new);

        Arrays.stream(GameMode.values()).forEach(gameMode -> {
            var holder = new BukkitGameModeHolder(gameMode);
            mapping.put(NamespacedMappingKey.of(gameMode.name()), holder);
            values.add(holder);
        });
    }
}
