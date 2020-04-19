package org.screamingsandals.gamecore.store;

import lombok.Data;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.screamingsandals.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.gamecore.team.GameTeam;

import java.io.File;
import java.io.Serializable;

@Data
public abstract class GameStore implements Serializable {
    private final LocationAdapter storeLocation;
    private final String shopName;
    private final File shopFile;
    private final GameTeam gameTeam;
    private final StoreType storeType;
    private EntityType entityType;
    private Villager.Profession profession = Villager.Profession.FARMER;

    private transient LivingEntity livingEntity;

    public GameStore(LocationAdapter storeLocation, File shopFile, String shopName, GameTeam gameTeam, StoreType storeType, EntityType entityType) {
        if (entityType == null || !entityType.isAlive()) {
            entityType = EntityType.VILLAGER;
        }

        this.storeLocation = storeLocation;
        this.shopFile = shopFile;
        this.gameTeam = gameTeam;
        this.shopName = shopName;
        this.storeType = storeType;
        this.entityType = entityType;
    }

    public void setEntityType(EntityType entityType) {
        if (entityType != null && entityType.isAlive()) {
            this.entityType = entityType;
        }
    }

    public LivingEntity spawn() {
        if (livingEntity == null) {
            livingEntity = (LivingEntity) storeLocation.getWorld().spawnEntity(storeLocation.getLocation(), entityType);
            if (shopName != null) {
                livingEntity.setCustomName(shopName);
                livingEntity.setCustomNameVisible(true);
            }
            if (livingEntity instanceof Villager) {
                ((Villager) livingEntity).setProfession(profession);
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
}
