package org.screamingsandals.lib.bukkit.event.entity;

import lombok.*;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntitySpawnEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntitySpawnEvent implements SEntitySpawnEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntitySpawnEvent event;

    // Internal cache
    private EntityBasic entity;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }
}
