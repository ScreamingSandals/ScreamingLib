package org.screamingsandals.lib.gamecore.core.entities;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.papermc.lib.PaperLib;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.error.BaseError;
import org.screamingsandals.lib.gamecore.error.ErrorType;

import java.util.*;

/*
Support class for entity management.
If we are going to shutdown, make sure to delete all entities.
This will prevent shop-despawning issues and others.
 */
public class EntityManager {
    @Getter
    private final Multimap<GameFrame, Entity> registeredEntities = ArrayListMultimap.create();

    public void register(GameFrame gameFrame, Entity entity) {
        registeredEntities.put(gameFrame, entity);
    }

    public void unregister(Entity entity) {
        for (Entity found : registeredEntities.values()) {
            if (found.equals(entity)) {
                internalUnregister(found);
                break;
            }
        }
    }

    public void unregisterAll(GameFrame gameFrame) {
        final List<Entity> entities = new LinkedList<>();

        for (Map.Entry<GameFrame, Entity> entry : registeredEntities.entries()) {
            if (entry.getKey().getUuid().equals(gameFrame.getUuid())) {
                entities.add(entry.getValue());
            }
        }

        entities.forEach(this::internalUnregister);
        registeredEntities.removeAll(gameFrame);
    }

    public void unregisterAll() {
        registeredEntities.values().forEach(this::internalUnregister);
        registeredEntities.clear();
    }

    private void internalUnregister(Entity entity) {
        if (entity != null && !entity.isDead()) {
            final var location = entity.getLocation();
            final var asyncChunk = PaperLib.getChunkAtAsync(location, false);

            if (asyncChunk.isDone()) {
                try {
                    final var chunk = asyncChunk.get();
                    chunk.load();
                } catch (Exception e) {
                    GameCore.getErrorManager().writeError(new BaseError(ErrorType.UNKNOWN, e), true);
                    final var nonAsyncChunk = location.getChunk();
                    nonAsyncChunk.load();
                }
            }
            entity.remove();
        }
    }

    public Collection<Entity> getEntities() {
        return registeredEntities.values();
    }
}
