package org.screamingsandals.gamecore.core;

import lombok.Data;
import org.screamingsandals.gamecore.GameCore;
import org.screamingsandals.gamecore.core.data.file.JsonDataSource;
import org.screamingsandals.gamecore.events.core.game.SGameLoadingEvent;
import org.screamingsandals.gamecore.events.core.game.SGameDisabledEvent;
import org.screamingsandals.lib.debug.Debug;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            JsonDataSource<T> dataSaver = new JsonDataSource<>(gameFile, type);
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

    public void loadGames() {
        if (dataFolder.exists()) {
            try (Stream<Path> stream = Files.walk(Paths.get(new File(getDataFolder(), "arenas").getAbsolutePath()))) {
                List<String> results = stream.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());

                if (results.isEmpty()) {
                    Debug.info("No arenas has been found!", true);
                } else {
                    results.forEach(result -> loadGame(new File(result)));
                }
            } catch (IOException e) {
                e.printStackTrace(); // maybe remove after testing
            }
        }
    }

    public void unregisterGames() {
        for (var game : registeredGames.values()) {
            unregisterGame(game);
        }
    }

    public void registerGame(String gameName, T gameFrame) {
        if (GameCore.fireEvent(new SGameLoadingEvent(gameFrame))) {
            return;
        }

        registeredGames.put(gameName, gameFrame);
    }

    public void unregisterGame(String gameName) {
        final var gameFrame = registeredGames.get(gameName);

        unregisterGame(gameFrame);
    }

    public void unregisterGame(T gameFrame) {
        if (gameFrame == null) {
            Debug.warn("GameFrame is null!", true);
            return;
        }

        if (GameCore.fireEvent(new SGameDisabledEvent(gameFrame))) {
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
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}