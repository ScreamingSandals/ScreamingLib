package org.screamingsandals.lib.gamecore.visuals;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.screamingsandals.lib.bossbars.bossbar.ScreamingBossbar;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.config.VisualsConfig;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

import java.util.UUID;

import static org.screamingsandals.lib.lang.I.m;

@EqualsAndHashCode(callSuper = false)
@Data
public class GameBossbar extends ScreamingBossbar implements GameVisual {
    private final GamePlayer gamePlayer;
    private final GameState gameState;
    private final VisualType visualType = VisualType.BOSSBAR;

    public GameBossbar(GamePlayer gamePlayer, GameState gameState,
                       String title, BarColor barColor, BarStyle barStyle, double progress) {
        this.gamePlayer = gamePlayer;
        this.gameState = gameState;
        originalTitle = title;
        identifier = gameState.getName();

        this.bossbarHolder.setBarColor(barColor);
        this.bossbarHolder.setBarStyle(barStyle);
        this.bossbarHolder.setProgress(progress);
        this.bossbarHolder.addViewer(gamePlayer.getPlayer());
        this.bossbarHolder.setVisible(false);

        update();
    }

    public void show() {
        update();

        bossbarHolder.setVisible(true);
        bossbarHolder.addViewer(gamePlayer.getPlayer());
    }

    public void hide() {
        bossbarHolder.setVisible(false);
        bossbarHolder.removeViewer();
    }

    public void update() {
        final var game = gamePlayer.getActiveGame();

        if (game == null) {
            return;
        }

        final var placeholderParser = gamePlayer.getActiveGame().getPlaceholderParser();
        bossbarHolder.setTitle(placeholderParser.parse(originalTitle));

        switch (gameState) {
            case PRE_GAME_COUNTDOWN:
                bossbarHolder.setProgress(game.getRawRemainingSeconds(), game.getStartTime());
                break;
            case IN_GAME:
                bossbarHolder.setProgress(game.getRawRemainingSeconds(), game.getGameTime());
                break;
            case DEATHMATCH:
                bossbarHolder.setProgress(game.getRawRemainingSeconds(), game.getDeathmatchTime());
                break;
            case AFTER_GAME_COUNTDOWN:
                bossbarHolder.setProgress(game.getRawRemainingSeconds(), game.getEndTime());
                break;
        }
    }

    @Data
    public static class Builder {
        private final UUID uuid;
        private final GameState gameState;

        public static GameBossbar get(GamePlayer gamePlayer, GameState gameState, GameFrame gameFrame) {
            final var uuid = gameFrame.getUuid();
            final var toReturn = new GameScoreboard.Builder(uuid, gameState);
            final var state = gameState.getName();
            final var visualsConfig = GameCore.getInstance().getVisualsConfig();
            final String title;
            final String color;

            if (toReturn.isCustomContentEnabled()) {
                title = visualsConfig.getString(VisualsConfig.PATH_BOSSBARS_CONTENT + state);
                color = visualsConfig.getString(VisualsConfig.PATH_BOSSBARS_COLOR + state);
            } else {
                title = m("bossbars.content." + state).get();
                color = m("bossbars.color." + state).get();
            }

            //TODO: handle this better
            final var barColor = BarColor.valueOf(color.toUpperCase());

            final var gameBossbar = new GameBossbar(gamePlayer, gameState, title, barColor, BarStyle.SOLID, 100);
            gameBossbar.update();

            return gameBossbar;
        }

        public boolean isCustomContentEnabled() {
            return GameCore.getInstance().getVisualsConfig().getBoolean(VisualsConfig.PATH_BOSSBARS_CUSTOM_ENABLED);
        }
    }
}