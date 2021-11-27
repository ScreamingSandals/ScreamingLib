package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.StriderTemperatureChangeEvent;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SStriderTemperatureChangeEvent;

@Data
public class SBukkitStriderTemperatureChangeEvent implements SStriderTemperatureChangeEvent {
    private final StriderTemperatureChangeEvent event;

    // Internal cache
    private EntityLiving entity;

    @Override
    public EntityLiving getEntity() {
        if (entity == null) {
            entity = EntityMapper.<EntityLiving>wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public boolean isShivering() {
        return event.isShivering();
    }
}
