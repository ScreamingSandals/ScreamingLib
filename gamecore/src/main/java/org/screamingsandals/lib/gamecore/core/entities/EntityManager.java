package org.screamingsandals.lib.gamecore.core.entities;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.world.BaseWorld;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
        registeredEntities.values().forEach(found -> {
            if (found.equals(entity)) {
                internalUnregister(found);
            }
        });
    }

    public void unregisterAll(GameFrame gameFrame) {
        final List<Entity> entities = new LinkedList<>();

        registeredEntities.entries().forEach(entry -> {
            if (entry.getKey().getUuid() == gameFrame.getUuid()) {
                entities.add(entry.getValue());
            }
        });

        entities.forEach(this::internalUnregister);
        registeredEntities.removeAll(gameFrame);
    }

    public void unregisterAll() {
        registeredEntities.values().forEach(this::internalUnregister);
        registeredEntities.clear();
    }

    private void internalUnregister(Entity entity) {
        if (entity != null && !entity.isDead()) {
            BaseWorld.getAndLoadChunkAsync(entity.getLocation());
            entity.remove();
        }
    }

    public Collection<Entity> getEntities() {
        return registeredEntities.values();
    }
}
