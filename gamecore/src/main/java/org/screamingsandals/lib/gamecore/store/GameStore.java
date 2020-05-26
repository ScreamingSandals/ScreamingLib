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
    private LocationAdapter location;
    private String name;
    private String pathToFile;
    private transient File storeFile;

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

    public GameStore(LocationAdapter location, String name, File storeFile, StoreType storeType) {
        this(location, name, storeFile, null, storeType);

    }

    public GameStore(LocationAdapter location, String name, File storeFile, GameTeam gameTeam, StoreType storeType) {
        this(location, name, storeFile, gameTeam, storeType, null);
    }

    public GameStore(LocationAdapter location, String name, File storeFile, GameTeam gameTeam, StoreType storeType, EntityType entityType) {
        this.location = location;
        this.name = name;
        this.storeFile = storeFile;
        this.pathToFile = storeFile.getPath();
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
        return spawn(gameFrame, name);
    }

    public LivingEntity spawn(GameFrame gameFrame, String shopName) {
        if (livingEntity == null) {
            livingEntity = (LivingEntity) location.getWorld().spawnEntity(location.getLocation(), entityType, CreatureSpawnEvent.SpawnReason.CUSTOM);
            livingEntity.setAI(false);
            livingEntity.setInvulnerable(true);
            livingEntity.setRemoveWhenFarAway(false);
            livingEntity.setRotation(location.getYaw(), location.getPitch());

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

    public File getStoreFile() {
        if (storeFile == null) {
            storeFile = new File(pathToFile);
        }
        return storeFile;
    }
}
