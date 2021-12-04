package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.DyeColor;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SSheepDyeWoolEvent;

@Data
public class SBukkitSheepDyeWoolEvent implements SSheepDyeWoolEvent, BukkitCancellable {
    private final SheepDyeWoolEvent event;

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
    public String getDyeColor() {
        return event.getColor().name();
    }

    @Override
    public void setDyeColor(String dyeColor) {
        event.setColor(DyeColor.valueOf(dyeColor.toUpperCase()));
    }
}
