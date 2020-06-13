package org.screamingsandals.lib.gamecore.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.config.custom.ScreamingConfigAdapter;

/**
 * GameConfig is responsible for per-game values.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class GameConfig extends ScreamingConfigAdapter {

    public interface DefaultKeys {
        String TEAMS_ENABLED = "teams";
        String STORES_ENABLED = "stores";
        String RESOURCES_ENABLED = "resources";
        String RESOURCES_PER_GAME = "per_game_resources";
        String SPECTATORS_ENABLED = "spectators";
        String START_TIME = "start_time";
        String GAME_TIME = "game_time";
        String DEATHMATCH_TIME = "deathmatch_time";
        String END_GAME_TIME = "end_game_time";
    }
}
