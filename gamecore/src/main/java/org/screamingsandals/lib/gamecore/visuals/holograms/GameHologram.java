package org.screamingsandals.lib.gamecore.visuals.holograms;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.nms.holograms.Hologram;
import org.screamingsandals.lib.nms.holograms.HologramManager;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class GameHologram extends Hologram {
    @Getter
    private final GameFrame gameFrame;
    @Getter
    private final HologramType hologramType;
    @Getter @Setter
    private UUID spawnerUuid; //Identifier for spawners or some other shits

    public GameHologram(GameFrame gameFrame, HologramType hologramType, HologramManager manager, List<Player> players,
                        Location loc, List<String> lines) {
        this(gameFrame, hologramType, manager, players, loc, lines, false);
    }

    public GameHologram(GameFrame gameFrame, HologramType hologramType,  HologramManager manager, List<Player> players,
                        Location location, List<String> lines, boolean touchable) {
        super(manager, players, location, lines, touchable);
        this.gameFrame = gameFrame;
        this.hologramType = hologramType;
    }

    public void setViewers(Collection<GamePlayer> gamePlayers) {
        final List<Player> toSet = new LinkedList<>();
        for (var gamePlayer : gamePlayers) {
            toSet.add(gamePlayer.getPlayer());
        }
        setViewers(toSet);
    }
}
