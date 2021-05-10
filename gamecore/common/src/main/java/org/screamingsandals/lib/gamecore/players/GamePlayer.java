package org.screamingsandals.lib.gamecore.players;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.gamecore.Game;
import org.screamingsandals.lib.gamecore.GameState;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.UUID;

@Getter
@Setter
public abstract class GamePlayer<P extends GamePlayer<P, G>, G extends Game<G, P, ? extends GameState>> extends PlayerWrapper {
    @Setter(AccessLevel.NONE)
    @Nullable
    private G game;
    private UUID latestGame;
    private boolean spectator;
    private boolean isTeleportingFromGame_justForInventoryPlugins;
    private boolean mainLobbyUsed;

    public GamePlayer(String name, UUID uuid) {
        super(name, uuid);
    }

    public void changeGame(G game) {
        if (this.game != null && game == null) {
            this.game.internalLeavePlayer(self());
            this.game = null;
            this.spectator = false;
            this.clean();
            /*if (Game.isBungeeEnabled()) {
                BungeeUtils.movePlayerToBungeeServer(as(Player.class), Main.isDisabling());
            } else {*/
                this.restoreInv();
            //}
        } else if (this.game == null && game != null) {
            this.storeInv();
            this.clean();
            this.game = game;
            this.spectator = false;
            this.mainLobbyUsed = false;
            this.game.internalJoinPlayer(self());
            if (this.game != null) {
                this.latestGame = this.game.getUuid();
            }
        } else if (this.game != null) {
            this.game.internalLeavePlayer(self());
            this.game = game;
            this.spectator = false;
            this.clean();
            this.mainLobbyUsed = false;
            this.game.internalJoinPlayer(self());
            if (this.game != null) {
                this.latestGame = this.game.getUuid();
            }
        }
    }

    public abstract void storeInv();

    public abstract void restoreInv();

    public abstract void resetLife();

    public abstract void invClean();

    public void clean() {
        invClean();
        resetLife();
    }

    public abstract void onDestroy();

    @SuppressWarnings("unchecked")
    public P self() {
        return (P) this;
    }

}
