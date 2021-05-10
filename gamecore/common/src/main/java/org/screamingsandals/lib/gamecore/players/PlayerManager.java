package org.screamingsandals.lib.gamecore.players;

import org.screamingsandals.lib.gamecore.Game;
import org.screamingsandals.lib.gamecore.GameCoreUtils;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ServiceDependencies(dependsOn = GameCoreUtils.class)
public abstract class PlayerManager<P extends GamePlayer<P, G>, G extends Game<G, P, ?>> {
    private final List<P> players = new ArrayList<>();

    public P getPlayerOrCreate(PlayerWrapper playerWrapper) {
        return getPlayer(playerWrapper)
                .orElseGet(() -> {
                    var p = createPlayerInstance(playerWrapper);
                    players.add(p);
                    return p;
                });
    }

    public Optional<P> getPlayer(UUID uuid) {
        return PlayerMapper.getPlayer(uuid).flatMap(this::getPlayer);
    }

    public Optional<P> getPlayer(PlayerWrapper playerWrapper) {
        return players.stream()
                .filter(gamePlayer -> gamePlayer.getUuid().equals(playerWrapper.getUuid()))
                .findFirst();
    }

    public boolean isPlayerInGame(PlayerWrapper playerWrapper) {
        return getPlayer(playerWrapper).map(p -> p.getGame() != null).orElse(false);
    }

    public boolean isPlayerInGame(UUID uuid) {
        return getPlayer(uuid).map(p -> p.getGame() != null).orElse(false);
    }

    public void dropPlayer(P player) {
        player.onDestroy();
        players.remove(player);
    }

    public boolean isPlayerRegistered(PlayerWrapper playerWrapper) {
        return getPlayer(playerWrapper).isPresent();
    }

    public boolean isPlayerRegistered(UUID uuid) {
        return getPlayer(uuid).isPresent();
    }

    protected abstract P createPlayerInstance(PlayerWrapper wrapper);

    public Optional<Game> getGameOfPlayer(UUID uuid) {
        return getPlayer(uuid).map(GamePlayer::getGame);
    }

    public Optional<Game> getGameOfPlayer(PlayerWrapper playerWrapper) {
        return getPlayer(playerWrapper).map(GamePlayer::getGame);
    }
}
