package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.HorseJumpEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SHorseJumpEvent;

@Data
public class SBukkitHorseJumpEvent implements SHorseJumpEvent, BukkitCancellable {
    private final HorseJumpEvent event;

    // Internal cache
    private EntityBasic entity;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public float getPower() {
        return event.getPower();
    }

    @Override
    public void setPower(float power) {
        event.setPower(power);
    }
}
