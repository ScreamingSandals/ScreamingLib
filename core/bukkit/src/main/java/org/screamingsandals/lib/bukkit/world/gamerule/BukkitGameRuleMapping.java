package org.screamingsandals.lib.bukkit.world.gamerule;

import org.bukkit.GameRule;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.gamerule.GameRuleMapping;

import java.util.Arrays;
import java.util.List;

@Service
public class BukkitGameRuleMapping extends GameRuleMapping {
    public BukkitGameRuleMapping() {
        if (Reflect.has("org.bukkit.GameRule")) {
            gameRuleConverter
                    .registerP2W(GameRule.class, BukkitGameRuleHolder::new);

            Arrays.stream(GameRule.values()).forEach(gameRule -> {
                var holder = new BukkitGameRuleHolder(gameRule);
                mapping.put(NamespacedMappingKey.of(gameRule.getName()), holder);
                values.add(holder);
            });
        } else {
            // bukkit api for legacy version didn't have proper game rules api
            // TODO: Actually fix this using NMS
            //Arrays.stream(Bukkit.getWorlds().get(0).getGameRules())
            List.of("doFireTick", "mobGriefing", "keepInventory", "doMobSpawning", "doMobLoot", "doTileDrops", "commandBlockOutput", "naturalRegeneration", "doDaylightCycle",
                            "logAdminCommands", "showDeathMessages", "randomTickSpeed", "sendCommandFeedback", "reducedDebugInfo", "doEntityDrops", "spectatorsGenerateChunks",
                            "spawnRadius", "disableElytraMovementCheck", "doWeatherCycle", "maxEntityCramming", "doLimitedCrafting", "maxCommandChainLength", "announceAdvancements",
                            "gameLoopFunction"
                    )
                    .forEach(gameRule -> {
                        var holder = new BukkitLegacyGameRuleHolder(gameRule);
                        mapping.put(NamespacedMappingKey.of(gameRule), holder);
                        values.add(holder);
                    });
        }
    }
}
