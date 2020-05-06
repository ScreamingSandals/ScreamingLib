package org.screamingsandals.lib.gamecore.core;

import lombok.Data;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.data.file.JsonDataSource;
import org.screamingsandals.lib.gamecore.error.ErrorManager;
import org.screamingsandals.lib.gamecore.events.core.game.SGameDisabledEvent;
import org.screamingsandals.lib.gamecore.events.core.game.SGameLoadingEvent;
import org.screamingsandals.lib.gamecore.events.core.game.SGameSavedEvent;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class GameManager<T extends GameFrame> {
    private final File dataFolder;
    private final Class<T> type;
    private Map<String, T> registeredGames = new HashMap<>();

    public GameManager(File dataFolder, Class<T> type) {
        this.dataFolder = dataFolder;
        this.type = type;
    }

    public T loadGame(File gameFile) {
        if (gameFile.exists() && gameFile.isFile()) {
            final JsonDataSource<T> dataSaver = new JsonDataSource<>(gameFile, type);
            final T game = dataSaver.load();

            if (game.checkIntegrity()) {
                Debug.warn("Cannot load game " + game.getDisplayedName() + "!");
                return null;
            }

            registerGame(game.getGameName(), game);
            return game;
        }
        return null;
    }

    public void saveGame(T gameFrame) {
        final JsonDataSource<T> dataSaver = new JsonDataSource<>(dataFolder, type);
        dataSaver.save(gameFrame);

        GameCore.fireEvent(new SGameSavedEvent(gameFrame));
    }

    public void loadGames() {
        if (dataFolder.exists()) {
            try (Stream<Path> stream = Files.walk(Paths.get(dataFolder.getAbsolutePath()))) {
                final List<String> results = stream.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());

                if (results.isEmpty()) {
                    Debug.info("No arenas has been found!", true);
                } else {
                    results.forEach(result -> loadGame(new File(result)));
                }
            } catch (Exception e) {
                GameCore.getInstance().getErrorManager().newError(ErrorManager.newEntry(ErrorManager.Type.GAME_LOADING_ERROR, e));
            }
        } else {
            Debug.info("No arenas has been found!", true);
        }
    }

    public void unregisterGames() {
        for (var game : registeredGames.values()) {
            unregisterGame(game, false);
        }
    }

    public void registerGame(String gameName, T gameFrame) {
        if (!GameCore.fireEvent(new SGameLoadingEvent(gameFrame))) {
            return;
        }

        gameFrame.start();
        registeredGames.put(gameName, gameFrame);
    }

    public void unregisterGame(String gameName) {
        final var gameFrame = registeredGames.get(gameName);

        unregisterGame(gameFrame, true);
    }

    public void unregisterGame(T gameFrame, boolean event) {
        if (gameFrame == null) {
            Debug.warn("GameFrame is null!", true);
            return;
        }

        if (event && !GameCore.fireEvent(new SGameDisabledEvent(gameFrame))) {
            return;
        }

        final String gameName = gameFrame.getGameName();

        gameFrame.stop();
        registeredGames.remove(gameName);
    }

    public T getFirstAvailableGame() {
        for (var game : registeredGames.values()) {
            if (game.getActiveState() == GameState.WAITING) {
                return game;
            }
        }
        return null;
    }

    public T castGame(GameFrame gameFrame) {
        try {
            return type.cast(gameFrame);
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
        return null;
    }

    public Collection<T> getRegisteredGames() {
        return registeredGames.values();
    }

    public Map<String, T> getRegisteredGamesMap() {
        return registeredGames;
    }
}