package org.screamingsandals.gamecore.core;

import lombok.Data;
import org.screamingsandals.gamecore.core.data.JsonDataSaver;
import org.screamingsandals.lib.debug.Debug;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class GameManager<T extends GameFrame> {
    private final File dataFolder;
    private final Class<T> type;
    //private final ItemSpawnerTypes itemSpawnerTypes;
    private Map<String, T> registeredGames = new HashMap<>();

    public GameManager(File dataFolder, Class<T> type) {
        this.dataFolder = dataFolder;
        this.type = type;
        //itemSpawnerTypes = ItemSpawnerTypes.load();

    }

    public void loadGames() {
        if (dataFolder.exists()) {
            try (Stream<Path> stream = Files.walk(Paths.get(new File(getDataFolder(), "arenas").getAbsolutePath()))) {
                List<String> results = stream.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());

                if (results.isEmpty()) {
                    Debug.info("No arenas has been found!", true);
                } else {
                    for (var result : results) {
                        final File file = new File(result);
                        if (file.exists() && file.isFile()) {
                            JsonDataSaver<T> dataSaver = new JsonDataSaver<>(file, type);
                            final T game = dataSaver.load();

                            if (game.checkIntegrity()) {
                                Debug.warn("Cannot load game " + game.getDisplayedName() + "!");
                                continue;
                            }

                            registerGame(game.getGameName(), game);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // maybe remove after testing
            }
        }
    }

    public void unloadGames() {
        for (var game : registeredGames.values()) {
            unregisterGame(game);
        }
    }

    public void registerGame(String gameName, T gameFrame) {
        registeredGames.put(gameName, gameFrame);

        gameFrame.start();
    }

    public void unregisterGame(String gameName) {
        final var gameFrame = registeredGames.get(gameName);
        if (gameFrame != null) {
            gameFrame.stop();
        }

        registeredGames.remove(gameName);
    }

    public void unregisterGame(T gameFrame) {
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