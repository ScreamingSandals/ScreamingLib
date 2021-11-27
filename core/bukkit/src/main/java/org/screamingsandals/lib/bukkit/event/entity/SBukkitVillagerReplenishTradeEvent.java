package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.event.player.SBukkitPlayerCraftItemEvent;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SVillagerReplenishTradeEvent;
import org.screamingsandals.lib.event.player.SPlayerCraftItemEvent;

@Data
public class SBukkitVillagerReplenishTradeEvent implements SVillagerReplenishTradeEvent, BukkitCancellable {
    private final VillagerReplenishTradeEvent event;

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

    @Override
    public int getBonus() {
        return event.getBonus();
    }

    @Override
    public void setBonus(int bonus) {
        event.setBonus(bonus);
    }
}
