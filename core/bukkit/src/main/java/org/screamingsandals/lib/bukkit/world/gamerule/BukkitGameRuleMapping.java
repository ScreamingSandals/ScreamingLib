package org.screamingsandals.lib.bukkit.world.gamerule;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;
import org.screamingsandals.lib.world.gamerule.GameRuleMapping;

import java.util.Arrays;

@Service
public class BukkitGameRuleMapping extends GameRuleMapping {
    public BukkitGameRuleMapping() {
        if (Reflect.has("org.bukkit.GameRule")) {
            gameRuleConverter
                    .registerP2W(GameRule.class, gameRule -> new GameRuleHolder(gameRule.getName()))
                    .registerW2P(GameRule.class, dimensionHolder -> GameRule.getByName(dimensionHolder.getPlatformName()));

            Arrays.stream(GameRule.values()).forEach(gameRule -> mapping.put(NamespacedMappingKey.of(gameRule.getName()), new GameRuleHolder(gameRule.getName())));
        } else {
            // bukkit api for legacy version didn't have proper game rules api
            Arrays.stream(Bukkit.getWorlds().get(0).getGameRules()).forEach(gameRule -> mapping.put(NamespacedMappingKey.of(gameRule), new GameRuleHolder(gameRule)));
        }

        gameRuleConverter
                .registerW2P(String.class, GameRuleHolder::getPlatformName);
    }
}
