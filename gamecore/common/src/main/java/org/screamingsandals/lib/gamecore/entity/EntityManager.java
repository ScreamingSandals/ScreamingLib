package org.screamingsandals.lib.gamecore.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.gamecore.Game;
import org.screamingsandals.lib.gamecore.GameManager;
import org.screamingsandals.lib.gamecore.players.GamePlayer;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.utils.annotations.Service;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service(dependsOn = {
        GameManager.class
})
@RequiredArgsConstructor
public class EntityManager {
    private final GameManager<?,?,?> gameManager;
    private final Map<EntityBasic, GameEntityStorage> map = new HashMap<>();

    public static EntityManager getInstance() {
        return ServiceManager.get(EntityManager.class);
    }

    public <G extends Game<G, P, ?>, P extends GamePlayer<P, G>> List<EntityBasic> getAllEntitiesInGame(G game) {
        return map.entrySet()
                .stream()
                .filter(entry -> game.getUuid().equals(entry.getValue().getGameUuid())).map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void putEntityToGame(EntityBasic entity, UUID gameUUID) {
        map.put(entity, new GameEntityStorage(gameUUID));
    }

    public <G extends Game<G, P, ?>, P extends GamePlayer<P, G>> void putEntityToGame(EntityBasic entity, G game) {
        putEntityToGame(entity, game.getUuid());
    }

    public Optional<GameEntityStorage> getEntityDetails(EntityBasic entity) {
        return Optional.ofNullable(map.get(entity));
    }

    public <G extends Game<G, P, ?>, P extends GamePlayer<P, G>> Optional<G> getGameOfEntity(EntityBasic entity) {
        return getEntityDetails(entity).map(GameEntityStorage::getGame);
    }

    public boolean isEntityInGame(EntityBasic entity) {
        return getGameOfEntity(entity).isPresent();
    }

    public <G extends Game<G, P, ?>, P extends GamePlayer<P, G>> void destroyEntitiesForGame(G game) {
        getAllEntitiesInGame(game).forEach(this::destroyEntity);
    }

    public <G extends Game<G, P, ?>, P extends GamePlayer<P, G>> void destroyEntity(EntityBasic entity) {
        if (!entity.isDead()) {
            entity.remove();
        }
        map.remove(entity);
    }

    @Data
    public class GameEntityStorage {
        private final UUID gameUuid;
        private Function<GamePlayer<?,?>, Boolean> onEntityClick = p -> false;
        private final ConfigurationNode node = BasicConfigurationNode.root();

        @SuppressWarnings("unchecked")
        public <G extends Game<G, P, ?>, P extends GamePlayer<P, G>> void setOnEntityClick(Function<P, Boolean> function) {
            onEntityClick = (Function<GamePlayer<?,?>, Boolean>) function;
        }

        @SuppressWarnings("unchecked")
        public <G extends Game<G, P, ?>, P extends GamePlayer<P, G>> Function<P, Boolean> getOnEntityClick() {
            return (Function<P, Boolean>) onEntityClick;
        }

        @SuppressWarnings("unchecked")
        public <G extends Game<G, P, ?>, P extends GamePlayer<P, G>> G getGame() {
            return (G) gameManager.getGame(gameUuid).orElseThrow();
        }
    }
}
