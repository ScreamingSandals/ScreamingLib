package org.screamingsandals.lib.gamecore.store;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;

@EqualsAndHashCode(callSuper = false)
@Data
public class VillagerStore extends GameStore {
    private Villager.Profession profession = Villager.Profession.FARMER;

    @Override
    public LivingEntity spawn() {
        LivingEntity livingEntity = super.spawn();

        if (livingEntity instanceof Villager) {
            ((Villager) livingEntity).setProfession(Villager.Profession.FARMER);
        }

        return livingEntity;
    }

    public static VillagerStore get(StoreBuilder storeBuilder) {
        final VillagerStore villagerStore = new  VillagerStore();
        villagerStore.setStoreLocation(storeBuilder.getStoreLocation());
        villagerStore.setShopName(storeBuilder.getShopName());
        villagerStore.setShopFile(storeBuilder.getShopFile());
        villagerStore.setGameTeam(storeBuilder.getGameTeam());
        villagerStore.setStoreType(storeBuilder.getStoreType());
        villagerStore.setEntityType(EntityType.VILLAGER);
        villagerStore.setProfession(storeBuilder.getProfession());

        return villagerStore;
    }
}
