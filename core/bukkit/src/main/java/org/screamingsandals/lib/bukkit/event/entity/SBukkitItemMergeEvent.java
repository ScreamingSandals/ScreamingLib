package org.screamingsandals.lib.bukkit.event.entity;

import lombok.*;
import org.bukkit.event.entity.ItemMergeEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityItem;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.entity.SItemMergeEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitItemMergeEvent implements SItemMergeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final ItemMergeEvent event;

    // Internal cache
    private EntityItem entity;
    private EntityItem target;

    @Override
    public EntityItem getEntity() {
        if (entity == null) {
            entity = new BukkitEntityItem(event.getEntity());
        }
        return entity;
    }

    @Override
    public EntityItem getTarget() {
        if (target == null) {
            target = new BukkitEntityItem(event.getTarget());
        }
        return target;
    }
}
