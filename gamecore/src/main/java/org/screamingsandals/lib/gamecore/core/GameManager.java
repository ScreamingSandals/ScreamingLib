package org.screamingsandals.lib.gamecore.core;

import lombok.Data;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.config.GameConfig;
import org.screamingsandals.lib.gamecore.core.cycle.GameCycleType;
import org.screamingsandals.lib.gamecore.core.data.file.JsonDataSource;
import org.screamingsandals.lib.gamecore.error.ErrorType;
import org.screamingsandals.lib.gamecore.error.GameError;
import org.screamingsandals.lib.gamecore.events.core.game.SGameDisabledEvent;
import org.screamingsandals.lib.gamecore.events.core.game.SGameLoadingEvent;
import org.screamingsandals.lib.gamecore.events.core.game.SGameSavedEvent;
import org.screamingsandals.lib.gamecore.resources.SpawnerEditor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class GameManager<T extends GameFrame> {
    private final GameConfig gameConfig;
    private final File dataFolder;
    private final Class<T> type;
    private final Map<UUID, Object> gameBuilders = new HashMap<>();
    private Map<UUID, T> registeredGames = new HashMap<>();
    private GameCycleType gameCycleType;

    public GameManager(File dataFolder, Class<T> type, GameCycleType gameCycleType) {
        this.gameConfig = new GameConfig();
        this.dataFolder = dataFolder;
        this.type = type;
        this.gameCycleType = gameCycleType;

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public T loadGame(File gameFile) {
        if (gameFile.exists()
                && gameFile.isFile()
                && gameFile.getName().endsWith(".json")) {
            final var dataSaver = new JsonDataSource<>(gameFile, type);
            final T game = dataSaver.load();

            if (game == null) {
                Debug.warn("&cDeserializing the game went wrong.");
                return null;
            }

            final var gameName = game.getGameName();
            if (!game.checkIntegrity(true)) {
                Debug.warn("&cCannot load game &e" + gameName);
                return null;
            }

            Debug.info("&aGame &e" + gameName + "&a has been loaded!", true);
            registerGame(game.getUuid(), game);
            return game;
        }
        return null;
    }

    public <K extends GameFrame> void saveGame(K gameFrame) {
        if (gameFrame == null) {
            Debug.warn("nothing to save!"); //TODO
            return;
        }

        final var gameFile = new File(dataFolder, gameFrame.getGameName() + ".json");
        final var dataSaver = new JsonDataSource<>(gameFile, type);
        dataSaver.save(gameFrame);

        GameCore.fireEvent(new SGameSavedEvent(gameFrame));
    }

    public void loadGames() {
        if (dataFolder.exists()) {
            try (Stream<Path> stream = Files.walk(Paths.get(dataFolder.getAbsolutePath()))) {
                final List<String> results = stream.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());

                if (results.isEmpty()) {
                    Debug.info("&cWe did not find any arena.. &e:(", true);
                } else {
                    results.forEach(result -> loadGame(new File(result)));
                }
            } catch (Exception e) {
                GameCore.getErrorManager().newError(new GameError(null, ErrorType.GAME_LOADING_ERROR, e), true);
            }
        } else {
            Debug.info("&cWe did not find any arena.. &e:(", true);
        }
    }

    public void unregisterAll() {
        registeredGames.values().forEach(GameFrame::stop);
        registeredGames.clear();
    }

    public void registerGame(UUID uuid, T gameFrame) {
        if (!GameCore.fireEvent(new SGameLoadingEvent(gameFrame))) {
            return;
        }

        gameFrame.start();
        registeredGames.put(uuid, gameFrame);
    }

    public void unregisterGame(UUID uuid) {
        final var gameFrame = registeredGames.get(uuid);

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

        gameFrame.stop();
        registeredGames.remove(gameFrame.getUuid());
    }

    public boolean isGameRegistered(UUID uuid) {
        return registeredGames.containsKey(uuid);
    }

    public boolean isGameRegistered(String gameName) {
        for (var registered : registeredGames.values()) {
            if (registered.getGameName().equals(gameName)) {
                return true;
            }
        }
        return false;
    }

    public Optional<T> getRegisteredGame(UUID uuid) {
        final var registered = registeredGames.get(uuid);

        if (registered != null) {
            return Optional.of(registered);
        }
        return Optional.empty();
    }

    public Optional<T> getRegisteredGame(String name) {
        for (var registered : registeredGames.values()) {
            if (registered.getGameName().equals(name)) {
                return Optional.of(registered);
            }
        }
        return Optional.empty();
    }

    public Optional<T> getFirstAvailableGame() {
        final TreeMap<Integer, T> availableGames = new TreeMap<>();
        registeredGames.values().forEach(gameFrame -> {
            if (gameFrame.getActiveState() != GameState.WAITING) {
                return;
            }

            availableGames.put(gameFrame.getPlayersInGame().size(), gameFrame);
        });

        if (availableGames.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(availableGames.lastEntry().getValue());
    }

    public Optional<T> castGame(GameFrame gameFrame) {
        try {
            return Optional.of(type.cast(gameFrame));
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
        return Optional.empty();
    }

    public Collection<T> getRegisteredGames() {
        return registeredGames.values();
    }

    public Set<String> getRegisteredGamesNames() {
        final Set<String> games = new HashSet<>();
        registeredGames.values().forEach(game -> games.add(game.getGameName()));

        return games;
    }

    public Map<UUID, T> getRegisteredGamesMap() {
        return registeredGames;
    }

    public void registerBuilder(UUID uuid, GameBuilder<?> gameBuilder) {
        gameBuilders.put(uuid, gameBuilder);
    }

    public void unregisterBuilder(UUID uuid) {
        gameBuilders.remove(uuid);
    }

    public boolean isInBuilder(UUID uuid) {
        return gameBuilders.containsKey(uuid);
    }

    public boolean isInBuilder(GameFrame gameFrame) {
        return isInBuilder(gameFrame.getUuid());
    }

    public boolean isInBuilder(String name) {
        for (var builder : gameBuilders.values()) {
            if (((GameBuilder<?>) builder).getGameName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <E extends GameBuilder<?>> E getGameBuilder(UUID uuid) {
        return (E) gameBuilders.get(uuid);
    }

    @SuppressWarnings("unchecked")
    public <E extends GameBuilder<?>> E getGameBuilder(String name) {
        for (var builder : gameBuilders.values()) {
            final var castedBuilder = (GameBuilder<?>) builder;
            if (castedBuilder.getGameName().equals(name)) {
                return (E) castedBuilder;
            }
        }

        return null;
    }

    public Set<String> getRegisteredBuildersNames() {
        final Set<String> games = new HashSet<>();
        gameBuilders.values().forEach(builder -> {
            final var castedBuilder = (GameBuilder<?>) builder;
            games.add(castedBuilder.getGameName());
        });

        return games;
    }

    public Optional<SpawnerEditor> getSpawnerEditor(Player player) {
        if (gameBuilders.isEmpty()) {
            return Optional.empty();
        }

        for (var builder : gameBuilders.values()) {
            final var castedBuilder = (GameBuilder<?>) builder;
            if (castedBuilder.getSpawnerEditor() == null) {
                continue;
            }

            final var editor = castedBuilder.getSpawnerEditor();
            if (editor.getPlayer() == player) {
                return Optional.of(editor);
            }
        }

        return Optional.empty();
    }
}