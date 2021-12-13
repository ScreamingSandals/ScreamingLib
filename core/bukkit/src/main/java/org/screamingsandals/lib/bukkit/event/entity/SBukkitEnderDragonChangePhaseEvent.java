package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEnderDragonChangePhaseEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEnderDragonChangePhaseEvent implements SEnderDragonChangePhaseEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EnderDragonChangePhaseEvent event;

    // Internal cache
    private EntityBasic entity;
    @Nullable
    private Phase currentPhase;
    private boolean currentPhaseCached;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    @Nullable
    public Phase getCurrentPhase() {
        if (!currentPhaseCached) {
            if (event.getCurrentPhase() != null) {
                currentPhase = Phase.valueOf(event.getCurrentPhase().name());
            }
            currentPhaseCached = true;
        }
        return currentPhase;
    }

    @Override
    public Phase getNewPhase() {
        return Phase.valueOf(event.getNewPhase().name());
    }

    @Override
    public void setNewPhase(Phase newPhase) {
        event.setNewPhase(EnderDragon.Phase.valueOf(newPhase.name()));
    }
}
