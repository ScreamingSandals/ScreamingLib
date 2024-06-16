package org.screamingsandals.lib.impl.bukkit.compat.v1_20_6;

import lombok.experimental.UtilityClass;
import org.bukkit.event.entity.EntityCombustEvent;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class EntityCombustEventCompat {
    public int getDuration(@NotNull EntityCombustEvent event) {
        return event.getDuration();
    }

    public void setDuration(@NotNull EntityCombustEvent event, int duration) {
        event.setDuration(duration);
    }
}
