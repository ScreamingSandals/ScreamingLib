package org.screamingsandals.lib.gamecore.store;

import lombok.Data;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.lang.Utils;

import java.io.File;
import java.io.Serializable;

@Data
public class GameStore implements Serializable {
    private LocationAdapter storeLocation;
    private String shopName;
    private String pathToFile;
    private transient File shopFile;

    private GameTeam gameTeam;
    private StoreType storeType;
    private EntityType entityType;
    private Villager.Profession profession = Villager.Profession.FARMER;

    private transient LivingEntity livingEntity;

    /*
    Internal use only.
     */
    @Deprecated
    public GameStore() {}

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
        this.pathToFile = shopFile.getPath();
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

    public LivingEntity spawn(GameFrame gameFrame) {
        return spawn(gameFrame, shopName);
    }

    public LivingEntity spawn(GameFrame gameFrame, String shopName) {
        if (livingEntity == null) {
            livingEntity = (LivingEntity) storeLocation.getWorld().spawnEntity(storeLocation.getLocation(), entityType, CreatureSpawnEvent.SpawnReason.CUSTOM);
            livingEntity.setAI(false);
            livingEntity.setInvulnerable(true);
            livingEntity.setRemoveWhenFarAway(false);
            livingEntity.setRotation(storeLocation.getYaw(), storeLocation.getPitch());

            if (shopName != null) {
                livingEntity.setCustomName(Utils.colorize(shopName));
                livingEntity.setCustomNameVisible(true);
                if (livingEntity instanceof Villager) {
                    ((Villager) livingEntity).setProfession(profession);
                }
            }
        }

        GameCore.getEntityManager().register(gameFrame.getUuid(), livingEntity);
        return livingEntity;
    }

    public void remove() {
        GameCore.getEntityManager().unregister(livingEntity);
    }

    public File getShopFile() {
        if (shopFile == null) {
            shopFile = new File(pathToFile);
        }
        return shopFile;
    }
}
