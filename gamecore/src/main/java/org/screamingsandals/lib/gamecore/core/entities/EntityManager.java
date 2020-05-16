package org.screamingsandals.lib.gamecore.core.entities;

import io.papermc.lib.PaperLib;
import org.bukkit.entity.Entity;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.error.BaseError;
import org.screamingsandals.lib.gamecore.error.ErrorType;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/*
Support class for entity management.
If we are going to shutdown, make sure to delete all entities.
This will prevent shop-despawning issues and others.
 */
public class EntityManager {
    private final List<Entity> registeredEntities = new LinkedList<>();

    public void registerEntity(Entity entity) {
        registeredEntities.add(entity);
    }

    public void unregisterEntity(Entity entity) {
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

        registeredEntities.remove(entity);
    }

    public void unregisterAll() {
        registeredEntities.forEach(this::unregisterEntity);
    }

    public Collection<Entity> getRegisteredEntities() {
        return new HashSet<>(registeredEntities);
    }
}
