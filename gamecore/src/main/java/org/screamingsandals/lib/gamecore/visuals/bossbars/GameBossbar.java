package org.screamingsandals.lib.gamecore.visuals.bossbars;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.screamingsandals.lib.bossbars.bossbar.ScreamingBossbar;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

@EqualsAndHashCode(callSuper = false)
@Data
public class GameBossbar extends ScreamingBossbar {
    private final GamePlayer gamePlayer;
    private final GameState gameState;

    public GameBossbar(GamePlayer gamePlayer, GameState gameState,
                       String title, BarColor barColor, BarStyle barStyle, double progress) {
        this.gamePlayer = gamePlayer;
        this.gameState = gameState;
        this.bossbarHolder.setTitle(title);
        this.bossbarHolder.setBarColor(barColor);
        this.bossbarHolder.setBarStyle(barStyle);
        this.bossbarHolder.setProgress(progress);
        this.bossbarHolder.addViewer(gamePlayer.getPlayer());
        this.bossbarHolder.setVisible(false);
    }

    public void update() {
        final var placeholderParser = gamePlayer.getActiveGame().getPlaceholderParser();
        bossbarHolder.setTitle(placeholderParser.parse(originalTitle));
    }
}
