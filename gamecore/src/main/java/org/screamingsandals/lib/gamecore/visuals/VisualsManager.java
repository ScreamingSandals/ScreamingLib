package org.screamingsandals.lib.gamecore.visuals;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Data;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
public class VisualsManager {
    private final GameFrame gameFrame;
    private final Multimap<UUID, GameVisual> activeVisuals = ArrayListMultimap.create();
    private final Multimap<UUID, GameVisual> savedVisuals = ArrayListMultimap.create();

    public void prepareGameVisuals(GamePlayer gamePlayer) {
        final var uuid = gamePlayer.getUuid();

        savedVisuals.put(uuid, GameScoreboard.Builder.get(gamePlayer, GameState.WAITING, gameFrame));
        savedVisuals.put(uuid, GameScoreboard.Builder.get(gamePlayer, GameState.PRE_GAME_COUNTDOWN, gameFrame));
        savedVisuals.put(uuid, GameScoreboard.Builder.get(gamePlayer, GameState.IN_GAME, gameFrame));
        savedVisuals.put(uuid, GameScoreboard.Builder.get(gamePlayer, GameState.DEATHMATCH, gameFrame));
        savedVisuals.put(uuid, GameScoreboard.Builder.get(gamePlayer, GameState.AFTER_GAME_COUNTDOWN, gameFrame));

        savedVisuals.put(uuid, GameBossbar.Builder.get(gamePlayer, GameState.WAITING, gameFrame));
        savedVisuals.put(uuid, GameBossbar.Builder.get(gamePlayer, GameState.PRE_GAME_COUNTDOWN, gameFrame));
        savedVisuals.put(uuid, GameBossbar.Builder.get(gamePlayer, GameState.IN_GAME, gameFrame));
        savedVisuals.put(uuid, GameBossbar.Builder.get(gamePlayer, GameState.DEATHMATCH, gameFrame));
        savedVisuals.put(uuid, GameBossbar.Builder.get(gamePlayer, GameState.AFTER_GAME_COUNTDOWN, gameFrame));
    }

    public void showGameVisuals() {
        savedVisuals.forEach((uuid, gameVisual) -> {
            if (gameVisual.getGameState() == gameFrame.getActiveState()) {
                show(uuid, gameVisual);
            }
        });
    }

    public void update() {
        gameFrame.getPlayersInGame().forEach(gamePlayer -> {
            final var uuid = gamePlayer.getUuid();

            if (activeVisuals.containsKey(uuid)) {
                final var isUpdated = new AtomicBoolean(false);
                activeVisuals.get(uuid).forEach(gameVisual -> isUpdated.set(updateIfIsSame(gameVisual)));

                if (isUpdated.get()) {
                    return;
                }

                hideAllForPlayer(uuid);
            }

            final var saved = savedVisuals.get(uuid);
            saved.forEach(gameVisual -> {
                if (gameVisual.getGameState() == gameFrame.getActiveState()) {
                    gameVisual.update();
                    show(uuid, gameVisual);
                }
            });
        });
    }

    public void destroy() {
        hideAll();

        savedVisuals.clear();
        activeVisuals.clear();
    }

    public void hideAll() {
        activeVisuals.values().forEach(GameVisual::hide);
    }

    public void hideAllForPlayer(GamePlayer gamePlayer) {
        hideAllForPlayer(gamePlayer.getUuid());
    }

    public void hideAllForPlayer(UUID uuid) {
        activeVisuals.get(uuid).forEach(GameVisual::hide);
        activeVisuals.removeAll(uuid);
    }

    public void remove(GamePlayer gamePlayer) {
        remove(gamePlayer.getUuid());
    }

    public void remove(UUID uuid) {
        hideAllForPlayer(uuid);

        activeVisuals.removeAll(uuid);
        savedVisuals.removeAll(uuid);
    }

    public void show(GamePlayer gamePlayer, GameVisual gameVisual) {
        show(gamePlayer.getUuid(), gameVisual);
    }

    public void show(UUID uuid, GameVisual gameVisual) {
        activeVisuals.put(uuid, gameVisual);
        gameVisual.show();
    }

    public void hide(GamePlayer gamePlayer, GameVisual gameVisual) {
        hide(gamePlayer.getUuid(), gameVisual);
    }

    public void hide(UUID uuid, GameVisual gameVisual) {
        activeVisuals.remove(uuid, gameVisual);
        gameVisual.hide();
    }

    private boolean updateIfIsSame(GameVisual gameVisual) {
        if (gameVisual == null) {
            return false;
        }

        if (gameVisual.getGameState() == gameFrame.getActiveState()) {
            gameVisual.update();
            return true;
        }
        return false;
    }
}
