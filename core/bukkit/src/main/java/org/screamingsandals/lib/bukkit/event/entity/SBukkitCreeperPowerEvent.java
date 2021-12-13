package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLightning;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SCreeperPowerEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitCreeperPowerEvent implements SCreeperPowerEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final CreeperPowerEvent event;

    // Internal cache
    private EntityBasic entity;
    @Nullable
    private EntityLightning bolt;
    private boolean boltCached;
    private PowerCause cause;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    @Nullable
    public EntityLightning getBolt() {
        if (!boltCached) {
            if (event.getLightning() != null) {
                bolt = EntityMapper.<EntityLightning>wrapEntity(event.getLightning()).orElseThrow();
            }
            boltCached = true;
        }
        return bolt;
    }

    @Override
    public PowerCause getCause() {
        if (cause == null) {
            cause = PowerCause.valueOf(event.getCause().name());
        }
        return cause;
    }
}
