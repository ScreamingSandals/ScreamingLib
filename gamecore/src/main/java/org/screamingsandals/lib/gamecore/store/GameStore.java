package org.screamingsandals.lib.gamecore.store;

import lombok.Data;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.lang.Base;

import java.io.File;
import java.io.Serializable;

@Data
public class GameStore implements Serializable {
    private final LocationAdapter storeLocation;
    private String shopName;
    private File shopFile;

    private GameTeam gameTeam;
    private StoreType storeType;
    private EntityType entityType;
    private Villager.Profession profession = Villager.Profession.FARMER;

    private transient LivingEntity livingEntity;

    public GameStore(LocationAdapter storeLocation, String shopName, File shopFile, StoreType storeType) {
        this(storeLocation, shopName, shopFile, null, storeType);
    }

    public GameStore(LocationAdapter storeLocation, String shopName, File shopFile, GameTeam gameTeam, StoreType storeType) {
        this(storeLocation, shopName, shopFile, gameTeam, storeType, null);
    }

    public GameStore(LocationAdapter storeLocation, String shopName, File shopFile, GameTeam gameTeam, StoreType storeType, EntityType entityType) {
        this.storeLocation = storeLocation;
        this.shopName = shopName;
        this.shopFile = shopFile;
        this.gameTeam = gameTeam;
        this.storeType = storeType;
        setEntityType(entityType);
    }

    public void setEntityType(EntityType entityType) {
        if (entityType != null && entityType.isAlive()) {
            this.entityType = entityType;
        } else {
            this.entityType = EntityType.VILLAGER;
        }
    }

    public LivingEntity spawn() {
        return spawn(shopName);
    }

    public LivingEntity spawn(String shopName) {
        if (livingEntity == null) {
            livingEntity = (LivingEntity) storeLocation.getWorld().spawnEntity(storeLocation.getLocation(), entityType);
            livingEntity.setAI(false);
            livingEntity.setInvulnerable(true);
            livingEntity.setRemoveWhenFarAway(false);
            livingEntity.setRotation(storeLocation.getYaw(), storeLocation.getPitch());

            if (shopName != null) {
                livingEntity.setCustomName(Base.translateColors(shopName));
                livingEntity.setCustomNameVisible(true);
                if (livingEntity instanceof Villager) {
                    ((Villager) livingEntity).setProfession(profession);
                }
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
