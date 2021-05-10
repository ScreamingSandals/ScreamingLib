package org.screamingsandals.lib.gamecore.team;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.gamecore.Game;
import org.screamingsandals.lib.gamecore.elements.GameElement;
import org.screamingsandals.lib.gamecore.elements.GameElementValue;
import org.screamingsandals.lib.gamecore.players.GamePlayer;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.SForwardingAudience;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class Team<P extends GamePlayer<P, G>, G extends Game<G, P, ?>> extends GameElement implements SForwardingAudience {
    private final GameElementValue<Integer> maxPlayersValue = new GameElementValue<>(1);
    private final GameElementValue<List<LocationHolder>> spawnsValue = new GameElementValue<>();

    private final List<P> players = new LinkedList<>();

    public Team(UUID uuid) {
        super(uuid);
    }

    public int getMaxPlayers() {
        return maxPlayersValue.get();
    }

    public List<LocationHolder> getSpawns() {
        return spawnsValue.get();
    }

    @Override
    @NotNull
    public Iterable<CommandSenderWrapper> audiences() {
        return List.copyOf(players);
    }
}
