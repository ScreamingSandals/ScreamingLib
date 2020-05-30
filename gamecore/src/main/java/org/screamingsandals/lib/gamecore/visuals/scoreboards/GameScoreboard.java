package org.screamingsandals.lib.gamecore.visuals.scoreboards;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.scoreboard.DisplaySlot;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.config.VisualsConfig;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.scoreboards.holder.ScoreboardHolder;
import org.screamingsandals.lib.scoreboards.scoreboard.ScreamingScoreboard;

import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import static org.screamingsandals.lib.lang.I.m;

@EqualsAndHashCode(callSuper = false)
@Data
public class GameScoreboard extends ScreamingScoreboard {
    private final GamePlayer gamePlayer;
    private final GameState gameState;

    public GameScoreboard(GamePlayer gamePlayer, GameState gameState, String displayName, DisplaySlot displaySlot,
                          TreeMap<Integer, String> lines) {
        this.gamePlayer = gamePlayer;
        this.gameState = gameState;
        this.identifier = gameState.getName();
        this.scoreboardHolder.setDisplayedName(displayName);
        this.scoreboardHolder.setDisplaySlot(displaySlot);
        this.scoreboardHolder.setOriginalLines(lines);
    }

    public void update(GameFrame gameFrame) {
        paintLines(gameFrame.getPlaceholderParser().getAvailable());
    }

    public void addTeams(List<GameTeam> gameTeams) {
        gameTeams.forEach(gameTeam -> addTeam(gameTeam.getTeamName(), gameTeam.getTeamColor().chatColor));
    }

    @Data
    public static class ContentBuilder {
        private final UUID uuid;
        private final GameState gameState;

        public static GameScoreboard get(GamePlayer gamePlayer, GameState gameState, GameFrame gameFrame) {
            final var uuid = gameFrame.getUuid();
            final var toReturn = new ContentBuilder(uuid, gameState);
            final var state = gameState.getName();
            final var visualsConfig = GameCore.getInstance().getVisualsConfig();
            final String displayName;
            final List<String> lines;

            if (toReturn.isCustomContentEnabled()) {
                displayName = visualsConfig.getString(VisualsConfig.PATH_SCOREBOARDS_NAME);
                lines = visualsConfig.getStringList(VisualsConfig.PATH_SCOREBOARDS_CONTENT + state);
            } else {
                displayName = m("scoreboards.name").get();
                lines = m("scoreboards.content." + state).getList();
            }

            final var gameScoreboard = new GameScoreboard(gamePlayer, toReturn.gameState, displayName, DisplaySlot.SIDEBAR,
                            ScoreboardHolder.sortLines(lines));
            gameScoreboard.update(gameFrame);

            return gameScoreboard;
        }

        public boolean isCustomContentEnabled() {
            return GameCore.getInstance().getVisualsConfig().getBoolean(VisualsConfig.PATH_SCOREBOARDS_CUSTOM_ENABLED);
        }
    }
}
