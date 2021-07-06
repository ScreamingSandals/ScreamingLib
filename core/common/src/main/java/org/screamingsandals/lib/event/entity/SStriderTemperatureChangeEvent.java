package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.annotations.LimitedVersionSupport;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
@LimitedVersionSupport("Bukkit >= 1.16")
public class SStriderTemperatureChangeEvent extends AbstractEvent {
    private final EntityLiving entity;
    private final boolean shivering;
}
