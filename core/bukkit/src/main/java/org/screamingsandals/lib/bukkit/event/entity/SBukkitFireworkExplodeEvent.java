package org.screamingsandals.lib.bukkit.event.entity;

import lombok.*;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityFirework;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SFireworkExplodeEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitFireworkExplodeEvent implements SFireworkExplodeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final FireworkExplodeEvent event;

    // Internal cache
    private EntityFirework entity;

    @Override
    public EntityFirework getEntity() {
        if (entity == null) {
            entity = EntityMapper.<EntityFirework>wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }
}
