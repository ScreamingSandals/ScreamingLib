package org.screamingsandals.lib.bukkit.event.entity;

import lombok.*;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SFoodLevelChangeEvent;
import org.screamingsandals.lib.item.Item;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitFoodLevelChangeEvent implements SFoodLevelChangeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final FoodLevelChangeEvent event;

    // Internal cache
    private EntityBasic entity;
    private Item item;
    private boolean itemCached;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public int getLevel() {
        return event.getFoodLevel();
    }

    @Override
    public void setLevel(int level) {
        event.setFoodLevel(level);
    }

    @Override
    @Nullable
    public Item getItem() {
        if (!itemCached) {
            if (event.getItem() != null) {
                item = new BukkitItem(event.getItem());
            }
            itemCached = true;
        }
        return item;
    }
}