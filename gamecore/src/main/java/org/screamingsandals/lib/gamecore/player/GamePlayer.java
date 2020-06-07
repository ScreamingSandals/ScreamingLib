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

    public boolean isInGame() {
        return activeGame != null;
    }

    //shortcut
    public boolean joinGame(GameFrame gameFrame) {
        return gameFrame.join(this);
    }

    //shortcut
    public boolean leaveGame() {
        if (activeGame != null) {
            activeGame.leave(this);
            return true;
        }
        return false;
    }

    public boolean joinTeam(GameTeam gameTeam) {
        return gameTeam.join(this);
    }

    public boolean leaveTeam() {
        //TODO: events
        if (gameTeam != null
                && activeGame != null) {
            gameTeam.leave(this);
            return true;
        }
        //TODO: events
        return false;
    }

    public void makePlayer() {
        spectator = false;
        clean(false);

        if (isInGame()) {
            teleport(gameTeam.getSpawn());
        } else {
            //todo - teleport to mainlobby
        }
        GameCore.fireEvent(new SPlayerSwitchedToPlayer(this));
    }

    public void makeSpectator(boolean fireEvent) {
        spectator = true;
        clean(true);

        teleport(activeGame.getGameWorld().getSpectatorSpawn());

        if (fireEvent) {
            GameCore.fireEvent(new SPlayerSwitchedToSpectator(this));
        }
    }

    public boolean teleport(Location location) {
        GameCore.fireEvent(new SPlayerTeleportEvent(activeGame,this, location, player.getLocation()));
        return PaperLib.teleportAsync(player, location, PlayerTeleportEvent.TeleportCause.PLUGIN).isDone();
    }

    public boolean teleport(LocationAdapter locationAdapter) {
        return teleport(locationAdapter.getLocation());
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

    public void createVisualsForGame(GameFrame gameFrame) {

    }
}
