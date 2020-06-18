package org.screamingsandals.lib.gamecore.player;

import io.papermc.lib.PaperLib;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.core.GameFrame;
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
    private PlayerState playerState;

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
            return activeGame.leave(this);
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
        clean(false);

        if (isInGame()) {
            playerState = PlayerState.ALIVE;
            teleport(activeGame.getGameWorld().getSpectatorSpawn())
                    .then(() -> GameCore.fireEvent(new SPlayerSwitchedToSpectator(this)))
                    .done();
        } else {
            playerState = PlayerState.NOT_TRACED;
            //GameCore.fireEvent(new SPlayerSwitchedToPlayer(this));
            //todo - teleport to mainlobby
        }
    }

    public void makeSpectator(boolean fireEvent) {
        playerState = PlayerState.SPECTATOR;
        clean(true);

        teleport(activeGame.getGameWorld().getSpectatorSpawn()).then(() -> {
            if (fireEvent) {
                GameCore.fireEvent(new SPlayerSwitchedToSpectator(this));
            }
        }).done();
    }

    public TeleportBuilder teleport(Location to) {
        return TeleportBuilder.get(this, to);
    }

    public TeleportBuilder teleport(LocationAdapter to) {
        return TeleportBuilder.get(this, to);
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

    public boolean isSpectator() {
        return playerState == PlayerState.SPECTATOR;
    }

    public boolean isAlive() {
        return playerState == PlayerState.ALIVE;
    }

    public boolean isTraced() {
        return playerState != PlayerState.NOT_TRACED;
    }

    @AllArgsConstructor
    public static class TeleportBuilder {
        private final GamePlayer gamePlayer;
        private Location to;
        private Runnable runnable;

        public static TeleportBuilder get(GamePlayer gamePlayer, Location to, Runnable runnable) {
            return new TeleportBuilder(gamePlayer, to, runnable);
        }

        public static TeleportBuilder get(GamePlayer gamePlayer, Location to) {
            return new TeleportBuilder(gamePlayer, to, null);
        }

        public static TeleportBuilder get(GamePlayer gamePlayer, LocationAdapter to) {
            return new TeleportBuilder(gamePlayer, to.getLocation(), null);
        }

        public static TeleportBuilder get(GamePlayer gamePlayer) {
            return new TeleportBuilder(gamePlayer, null, null);
        }

        public TeleportBuilder to(Location to) {
            this.to = to;
            return this;
        }

        public TeleportBuilder then(Runnable runnable) {
            this.runnable = runnable;
            return this;
        }

        public void done() {
            GameCore.fireEvent(new SPlayerTeleportEvent(gamePlayer.getActiveGame(), gamePlayer, to, gamePlayer.getPlayer().getLocation()));
            final var future = PaperLib.teleportAsync(gamePlayer.getPlayer(), to, PlayerTeleportEvent.TeleportCause.PLUGIN);

            if (runnable != null) {
                future.thenRun(runnable);
            }
        }
    }
}
