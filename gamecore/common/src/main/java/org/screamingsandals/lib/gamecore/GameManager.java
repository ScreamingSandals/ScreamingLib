package org.screamingsandals.lib.gamecore;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.gamecore.players.GamePlayer;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;
import org.screamingsandals.lib.utils.logger.LoggerWrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ServiceDependencies(dependsOn = GameCoreUtils.class)
public abstract class GameManager<G extends Game<G, P, GS>, P extends GamePlayer<P, G>, GS extends GameState> {
    protected final List<G> games = new LinkedList<>();

    protected abstract Path getArenasFolder();
    protected abstract LoggerWrapper getLogger();

    public Optional<G> getGame(String name) {
        return games.stream()
                .filter(game -> game.getName().map(s -> s.equals(name)).orElse(false))
                .findFirst();
    }

    public Optional<G> getGame(UUID uuid) {
        return games.stream().
                filter(game -> game.getUuid().equals(uuid))
                .findFirst();
    }

    public List<G> getGames() {
        return List.copyOf(games);
    }

    public List<String> getGameNames() {
        return games.stream()
                .map(Game::getName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public boolean hasGame(String name) {
        return getGame(name).isPresent();
    }

    public boolean hasGame(UUID uuid) {
        return getGame(uuid).isPresent();
    }

    public abstract Optional<G> loadGameFromFile(Path path);

    public void addGame(G game) {
        if (!games.contains(game)) {
            games.add(game);
        }
    }

    public void removeGame(@NotNull G game) {
        games.remove(game);
    }

    public abstract G createNewGame();

    @OnPostEnable
    public void onPostEnable() {
        if (Files.exists(getArenasFolder())) {
            try (var stream = Files.walk(getArenasFolder().toAbsolutePath())) {
                final var results = stream.filter(Files::isRegularFile)
                        .filter(path -> !path.getFileName().toString().toLowerCase().endsWith(".disabled"))
                        .collect(Collectors.toList());
                if (results.isEmpty()) {
                    getLogger().debug("No arenas have been found!");
                } else {
                    results.forEach(file -> loadGameFromFile(file).ifPresent(games::add));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnPreDisable
    public void onPreDisable() {
        games.forEach(Game::unload);
        games.clear();
    }
}
