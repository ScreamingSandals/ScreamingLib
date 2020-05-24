package org.screamingsandals.lib.gamecore.resources;

import org.bukkit.entity.Player;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.error.ErrorType;
import org.screamingsandals.lib.gamecore.error.GameError;
import org.screamingsandals.lib.gamecore.visuals.holograms.GameHologram;
import org.screamingsandals.lib.nms.holograms.Hologram;
import org.screamingsandals.lib.nms.holograms.TouchHandler;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.mpr;

public class SpawnerHologramHandler implements TouchHandler {

    @Override
    public void handle(Player player, Hologram hologram) {
        final var hologramManager = GameCore.getHologramManager();

        if (hologram instanceof GameHologram) {
            final var gameHologram = (GameHologram) hologram;
            if (hologramManager.getActiveHolograms().contains(gameHologram)
                    && GameCore.getGameManager().isInBuilder(gameHologram.getGameFrame())) {
                switch (gameHologram.getHologramType()) {
                    case BUILDER_SPAWNER:
                        handleBuilder(player, gameHologram);
                        break;
                    case GAME_SPAWNER:
                        handleGame(player, gameHologram);
                        break;
                }
            }
        }
    }

    private void handleBuilder(Player player, GameHologram gameHologram) {
        final var game = gameHologram.getGameFrame();
        final var spawnerUUID = gameHologram.getUuid();
        final var resourceSpawner = game.getResourceManager().getByID(spawnerUUID);

        if (resourceSpawner.isEmpty()) {
            return;
        }

        var gameBuilder = GameCore.getGameManager().getGameBuilder(game.getUuid());
        if (gameBuilder.getSpawnerEditor() != null) {
            mpr("game-builder.spawners.editor.already-active").sendList(player);
            return;
        }

        try {
            gameBuilder.setSpawnerEditor(new SpawnerEditor(player, gameBuilder, resourceSpawner.get(), gameHologram));
            mpr("game-builder.spawners.editor.started").sendList(player);
        } catch (Exception e) {
            GameCore.getErrorManager().newError(new GameError(gameBuilder.getGameFrame(), ErrorType.SPAWNER_EDITOR_FAILED, e));
        }
    }

    private void handleGame(Player player, GameHologram gameHologram) {
        //TODO: private team/player spawners
    }
}
