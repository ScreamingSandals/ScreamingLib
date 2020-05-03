package org.screamingsandals.lib.gamecore.store;

import lombok.Data;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.screamingsandals.lib.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.team.GameTeam;

import java.io.File;
import java.io.Serializable;

@Data
public abstract class GameStore implements Serializable {
    private LocationAdapter storeLocation;
    private String shopName;
    private File shopFile;

    private GameTeam gameTeam;
    private StoreType storeType;
    private EntityType entityType;

    private transient LivingEntity livingEntity;

    public void setEntityType(EntityType entityType) {
        if (entityType != null && entityType.isAlive()) {
            this.entityType = entityType;
        }
    }

    public static <T> T get(StoreBuilder storeBuilder) {
        return null;
    }

    public LivingEntity spawn() {
        if (livingEntity == null) {
            livingEntity = (LivingEntity) storeLocation.getWorld().spawnEntity(storeLocation.getLocation(), entityType);
            if (shopName != null) {
                livingEntity.setCustomName(shopName);
                livingEntity.setCustomNameVisible(true);
                livingEntity.setRemoveWhenFarAway(false);
            }
        }
        return livingEntity;
    }

    public LivingEntity kill() {
        final LivingEntity entity = livingEntity;

        if (livingEntity != null) {
            final Chunk chunk = livingEntity.getLocation().getChunk();

            if (!chunk.isLoaded()) {
                chunk.load();
            }

            livingEntity.remove();
            livingEntity = null;
        }
        return entity;
    }

    @Data
    public static class StoreBuilder {
        private LocationAdapter storeLocation;
        private String shopName;
        private File shopFile;

        private GameTeam gameTeam;
        private StoreType storeType;
        private EntityType entityType;
        private Villager.Profession profession = Villager.Profession.FARMER;

        public static StoreBuilder create() {
            return new StoreBuilder();
        }

        public StoreBuilder setLocation(LocationAdapter location) {
            storeLocation = location;
            return this;
        }

        public StoreBuilder setName(String shopName) {
            this.shopName = shopName;
            return this;
        }

        public StoreBuilder setShopFile(File shopFile) {
            this.shopFile = shopFile;
            return this;
        }

        public StoreBuilder setGameTeam(GameTeam gameTeam) {
            this.gameTeam = gameTeam;
            return this;
        }

        public StoreBuilder setStoreType(StoreType storeType) {
            this.storeType = storeType;
            return this;
        }

        public StoreBuilder setEntityType(EntityType entityType) {
            EntityType toSet = entityType;
            if (toSet == null || !toSet.isAlive()) {
                toSet = EntityType.VILLAGER;
            }

            this.entityType = toSet;
            return this;
        }

        public StoreBuilder setProfession(Villager.Profession profession) {
            this.profession = profession;
            return this;
        }

    }
}
