package org.screamingsandals.lib.gamecore.player;

import io.papermc.lib.PaperLib;
import lombok.Data;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.events.player.SPlayerSwitchedToPlayer;
import org.screamingsandals.lib.gamecore.events.player.SPlayerSwitchedToSpectator;
import org.screamingsandals.lib.gamecore.events.player.SPlayerTeleportEvent;
import org.screamingsandals.lib.gamecore.team.GameTeam;

import java.util.UUID;

@Data
@ToString(exclude = {"activeGame"})
public class GamePlayer {
    private final Player player;
    private final UUID uuid;
    private final PlayerStorage playerStorage;
    private GameTeam gameTeam;
    private GameFrame activeGame;
    private boolean spectator;

    public GamePlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.playerStorage = new PlayerStorage();
    }

    public void destroy() {
        if (gameTeam != null) {
            gameTeam.leave(this);
        }
    }

    public boolean isInGame() {
        return activeGame != null;
    }

    //shortcut
    public boolean join(GameFrame gameFrame) {
        return gameFrame.join(this);
    }

    //shortcut
    public boolean leave() {
        if (activeGame != null) {
            return activeGame.leave(this);
        }
        return false;
    }

    public void makePlayer() {
        spectator = false;
        clean(false);

        if (isInGame()) {
            teleport(gameTeam.getSpawnLocation());
        } else {
            //todo - teleport to mainlobby
        }
        GameCore.fireEvent(new SPlayerSwitchedToPlayer(this));
    }

    public void makeSpectator() {
        spectator = true;
        clean(true);

        teleport(activeGame.getGameWorld().getSpectatorSpawn());
        GameCore.fireEvent(new SPlayerSwitchedToSpectator(this));
    }

    public void teleport(Location location) {
        GameCore.fireEvent(new SPlayerTeleportEvent(this, location, player.getLocation()));
        PaperLib.teleportAsync(player, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public void teleport(LocationAdapter locationAdapter) {
        teleport(locationAdapter.getLocation());
    }

    public void sendMessage(String string) {
        player.sendMessage(string);
    }

    public void storeAndClean() {
        playerStorage.store(player);
        playerStorage.clean(player, false);
    }

    public void restore(boolean teleport) {
        playerStorage.restore(player, teleport);
    }

    public void clean(boolean spectator) {
        playerStorage.clean(player, spectator);
    }
}
