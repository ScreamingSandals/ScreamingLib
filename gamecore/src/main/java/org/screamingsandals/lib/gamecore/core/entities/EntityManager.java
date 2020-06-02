package org.screamingsandals.lib.gamecore.core.entities;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.world.regeneration.Regenerable;
import org.screamingsandals.lib.tasker.BaseTask;
import org.screamingsandals.lib.tasker.TaskerTime;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/*
Support class for entity management.
If we are going to shutdown, make sure to delete all entities.
This will prevent shop-despawning issues and others.
 */
public class EntityManager {
    @Getter
    private final Multimap<UUID, Entity> registeredEntities = ArrayListMultimap.create();

    public void register(UUID uuid, Entity entity) {
        registeredEntities.put(uuid, entity);
    }

    public void unregister(Entity entity) {
        registeredEntities.values().forEach(found -> {
            if (found.equals(entity)) {
                internalUnregister(found);
            }
        });
    }

    public void unregisterAll(UUID uuid) {
        final List<Entity> entities = new LinkedList<>();

        registeredEntities.entries().forEach(entry -> {
            if (entry.getKey() == uuid) {
                entities.add(entry.getValue());
            }
        });

        entities.forEach(this::internalUnregister);
        registeredEntities.removeAll(uuid);
    }

    public void unregisterAll() {
        registeredEntities.values().forEach(this::internalUnregister);
        registeredEntities.clear();
    }

    private void internalUnregister(Entity entity) {
        if (entity != null && !entity.isDead()) {
            if (Regenerable.loadChunkAsync(entity.getLocation())) {
                entity.remove();
            } else {
                GameCore.getTasker().runTaskLater(BaseTask.get(entity::remove), 1, TaskerTime.TICKS);
            }
        }
    }

    public Collection<Entity> getEntities() {
        return registeredEntities.values();
    }

    public boolean isRegisteredInGame(UUID uuid, Entity entity) {
        System.out.println(registeredEntities.get(uuid));
        System.out.println(registeredEntities.size());
        for (var registered : registeredEntities.get(uuid)) {
            if (registered.equals(entity)) {
                return true;
            }
        }
        return false;
    }
}
