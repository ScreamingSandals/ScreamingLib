package org.screamingsandals.lib.gamecore.listeners;

import lombok.experimental.UtilityClass;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.gamecore.GameCoreUtils;
import org.screamingsandals.lib.gamecore.entity.EntityManager;
import org.screamingsandals.lib.player.event.SPlayerInteractEntityEvent;
import org.screamingsandals.lib.utils.annotations.Service;

@Service(dependsOn = {
        EntityManager.class
})
@UtilityClass
public class ClickableEntityListener {
    @SuppressWarnings("ConstantConditions")
    @OnEvent
    public void onClick(SPlayerInteractEntityEvent event) {
        var playerManager = GameCoreUtils.getPlayerManager();
        if (playerManager.isPlayerInGame(event.getPlayer())) {
            var gamePlayer = playerManager.getPlayer(event.getPlayer()).orElseThrow();
            var game = gamePlayer.getGame();
            if (!gamePlayer.isSpectator() && game.getGameState() != null && game.getGameState().isGameRunning()) {
                EntityManager.getInstance().getEntityDetails(event.getClickedEntity()).ifPresent(gameEntityStorage -> {
                    if (gameEntityStorage.getGameUuid().equals(game.getUuid())
                            && gameEntityStorage.getOnEntityClick().apply(gamePlayer)) {
                                event.setCancelled(true);
                    }
                });
            }
        }
    }
}
