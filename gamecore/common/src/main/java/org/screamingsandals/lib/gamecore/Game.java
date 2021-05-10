package org.screamingsandals.lib.gamecore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.gamecore.cycles.Cycle;
import org.screamingsandals.lib.gamecore.elements.GameElement;
import org.screamingsandals.lib.gamecore.elements.GameElementGroupKey;
import org.screamingsandals.lib.gamecore.elements.GameElementManager;
import org.screamingsandals.lib.gamecore.players.GamePlayer;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.SForwardingAudience;
import org.screamingsandals.lib.utils.Nameable;
import org.screamingsandals.lib.utils.UniqueIdentifiable;

import java.util.*;
import java.util.stream.Collectors;

@Data
public abstract class Game<G extends Game<G, P, GS>, P extends GamePlayer<P, G>, GS extends GameState> implements UniqueIdentifiable, Nameable, SForwardingAudience {
    protected final UUID uuid;
    @Getter(AccessLevel.NONE)
    protected final Map<GameElementGroupKey<?>, GameElementManager<?>> gameElementManagers = new HashMap<>();
    protected final List<P> players = new LinkedList<>();
    protected String name;
    protected String displayName;
    @Nullable
    protected GS gameState;
    @Nullable
    protected Cycle<GS> cycle;

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getDisplayName() {
        return Optional.ofNullable(displayName).or(() -> Optional.ofNullable(name));
    }

    @SuppressWarnings("unchecked")
    public <E extends GameElement> GameElementManager<E> getGameElementManager(GameElementGroupKey<E> key) {
        if (!gameElementManagers.containsKey(key)) {
            gameElementManagers.put(key, new GameElementManager<>());
        }

        return (GameElementManager<E>) gameElementManagers.get(key);
    }

    @SuppressWarnings("unchecked")
    public <E extends GameElement> Map<GameElementGroupKey<E>, GameElementManager<E>> getGameElementManagers(Class<E> baseClass) {
        return gameElementManagers
                .entrySet()
                .stream()
                .filter(entry -> baseClass.isAssignableFrom(entry.getKey().getType()))
                .collect(Collectors.toMap(entry -> (GameElementGroupKey<E>) entry.getKey(), entry -> (GameElementManager<E>) entry.getValue()));
    }

    public void joinPlayer(P player) {
        if (player.getGame() != this) {
            player.changeGame(self());
        }
    }

    public void quitPlayer(P player) {
        if (player.getGame() == this) {
            player.changeGame(null);
        }
    }

    public abstract void internalJoinPlayer(P player);

    public abstract void internalLeavePlayer(P player);

    public void unload() {

    }

    @Override
    @NonNull
    public Iterable<CommandSenderWrapper> audiences() {
        return List.copyOf(players);
    }

    @SuppressWarnings("unchecked")
    public G self() {
        return (G) this;
    }
}
