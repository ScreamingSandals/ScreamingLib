package org.screamingsandals.lib.bukkit.hologram.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.screamingsandals.lib.bukkit.entity.BukkitDataWatcher;
import org.screamingsandals.lib.bukkit.utils.nms.entity.EntityNMS;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.utils.AdventureHelper;

import java.util.UUID;

public class FakeEntityNMS {
    public final BukkitDataWatcher dataWatcher;
    private final int id;
    private final UUID uuid;
    private Location location;
    private byte maskedByte = 0;
    private Component customName = Component.empty();

    public FakeEntityNMS(Location location) {
        id = EntityNMS.incrementAndGetId();
        this.customName = Component.empty();
        this.uuid = UUID.randomUUID();
        this.dataWatcher = new BukkitDataWatcher(null);
        this.location = location;
        dataWatcher.register(DataWatcher.Item.of(0, (byte) 0));
        dataWatcher.register(DataWatcher.Item.of(1, 300));
        dataWatcher.register(DataWatcher.Item.of(2, " "));
        dataWatcher.register(DataWatcher.Item.of(3, false));
        dataWatcher.register(DataWatcher.Item.of(4, false));
    }

    public void setInvisible(boolean invisible) {
        maskedByte = getMaskedByteFromBoolFlag(maskedByte, 5, invisible);
        dataWatcher.register(DataWatcher.Item.of(0, maskedByte));
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        dataWatcher.register(DataWatcher.Item.of(3, customNameVisible));
    }

    public void setGravity(boolean gravity) {
         dataWatcher.register(DataWatcher.Item.of(5, gravity));
    }

    public Component getCustomName() {
         return customName;
    }

    public void setCustomName(Component name) {
        var str = AdventureHelper.toLegacy(name);
        if (str.length() > 256) {
            str = str.substring(0, 256);
        }
        dataWatcher.register(DataWatcher.Item.of(2, str));
        customName = name;
    }

    public BukkitDataWatcher getDataWatcher() {
        return dataWatcher;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isOnGround() {
        return true;
    }

    public UUID getUuid() {
        return uuid;
    }

    private byte getMaskedByteFromBoolFlag(byte b, int i, boolean flag) {
        if (flag) {
            return (byte)(b | 1 << i);
        } else {
            return (byte)(b & (~(1 << i)));
        }
    }

}
