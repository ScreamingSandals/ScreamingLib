package org.screamingsandals.lib.bukkit.event.entity;

import lombok.*;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.event.player.SBukkitPlayerCraftItemEvent;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SVillagerAcquireTradeEvent;
import org.screamingsandals.lib.event.player.SPlayerCraftItemEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitVillagerAcquireTradeEvent implements SVillagerAcquireTradeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final VillagerAcquireTradeEvent event;

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
    public SPlayerCraftItemEvent.Recipe getRecipe() {
        return new SBukkitPlayerCraftItemEvent.BukkitRecipe(event.getRecipe());
    }

    @Override
    public void setRecipe(SPlayerCraftItemEvent.Recipe recipe) {
        event.setRecipe((MerchantRecipe) recipe.raw());
    }
}
