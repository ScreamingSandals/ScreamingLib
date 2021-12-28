package org.screamingsandals.lib.bukkit.world.difficulty;

import org.bukkit.Difficulty;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;
import org.screamingsandals.lib.world.difficulty.DifficultyMapping;

import java.util.Arrays;

@Service
public class BukkitDifficultyMapping extends DifficultyMapping {
    public BukkitDifficultyMapping() {
        difficultyConverter
                .registerP2W(Difficulty.class, BukkitDifficultyHolder::new);

        Arrays.stream(Difficulty.values()).forEach(difficulty -> {
            var holder = new BukkitDifficultyHolder(difficulty);
            mapping.put(NamespacedMappingKey.of(difficulty.name()), holder);
            values.add(holder);
        });
    }
}
