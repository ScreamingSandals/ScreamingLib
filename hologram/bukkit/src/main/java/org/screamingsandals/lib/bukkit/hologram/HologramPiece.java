package org.screamingsandals.lib.bukkit.hologram;

import com.google.common.base.Preconditions;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.bukkit.utils.nms.entity.EntityNMS;
import org.screamingsandals.lib.nms.accessors.ArmorStandAccessor;
import org.screamingsandals.lib.nms.accessors.EntityDataAccessorAccessor;
import org.screamingsandals.lib.packet.MetadataItem;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
public class HologramPiece {
    public static final int CLIENT_FLAGS_INDEX;
    public static final int HEAD_POSE_INDEX;

    static {
        final var object = Reflect.getField(ArmorStandAccessor.getFieldDATA_CLIENT_FLAGS());
        CLIENT_FLAGS_INDEX = (int) Reflect.fastInvoke(object, EntityDataAccessorAccessor.getMethodGetId1());

        final var object2 = Reflect.getField(ArmorStandAccessor.getFieldDATA_HEAD_POSE());
        HEAD_POSE_INDEX = (int) Reflect.fastInvoke(object2, EntityDataAccessorAccessor.getMethodGetId1());
    }

    private final int id;
    private LocationHolder location;
    private UUID uuid;
    private final List<MetadataItem> metadataItems;
    private byte maskedByte;
    private byte maskedByte2;
    private Component customName = Component.empty();
    private Vector3Df headPose;

    public HologramPiece(LocationHolder location) {
        this.id = EntityNMS.incrementAndGetId();
        this.location = location;
        this.uuid = UUID.randomUUID();
        this.headPose = new Vector3Df(0.0f, 0.0f, 0.0f);
        this.metadataItems = Collections.synchronizedList(new ArrayList<>());
        this.maskedByte = 0;
        this.maskedByte2 = 0;

        put(MetadataItem.of((byte) 0, (byte) 0));
        put(MetadataItem.of((byte) 1, 300));
        if (Version.isVersion(1, 13)) {
            put(MetadataItem.ofOpt((byte) 2, Component.empty()));
        } else {
            put(MetadataItem.of((byte) 2, " "));
        }
        put(MetadataItem.of((byte) 3, false));
        put(MetadataItem.of((byte) 4, false));
        put(MetadataItem.of((byte) CLIENT_FLAGS_INDEX, maskedByte2));
    }

    public void put(MetadataItem metadataItem) {
        metadataItems.removeIf(metadataItem1 -> metadataItem1.getIndex() == metadataItem.getIndex());
        metadataItems.add(metadataItem);
    }

    public void setInvisible(boolean invisible) {
        setMaskedByte1FromFlag(5, invisible);
        put(MetadataItem.of((byte) 0, maskedByte));
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        put(MetadataItem.of((byte) 3, customNameVisible));
    }

    public void setCustomName(Component name) {
        if (Version.isVersion(1, 13)) {
            put(MetadataItem.ofOpt((byte) 2, name));
        } else {
            var str = AdventureHelper.toLegacy(name);
            if (str.length() > 256) {
                str = str.substring(0, 256);
            }
            put(MetadataItem.of((byte) 2, str));
        }
        customName = name;
    }

    public void setSmall(boolean isSmall) {
        setMaskedByte2FromFlag(1, isSmall);
        put(MetadataItem.of((byte) CLIENT_FLAGS_INDEX, maskedByte2));
    }

    public void setGravity(boolean gravity) {
        if (Version.isVersion(1, 10)) {
            put(MetadataItem.of((byte) 5, !gravity));
        } else {
            setMaskedByte2FromFlag( 2, gravity);
            put(MetadataItem.of((byte) CLIENT_FLAGS_INDEX, maskedByte2));
        }
    }

    public void setArms(boolean hasArms) {
        setMaskedByte2FromFlag(4, hasArms);
        put(MetadataItem.of((byte) CLIENT_FLAGS_INDEX, maskedByte2));
    }

    public void setBasePlate(boolean hasBasePlate) {
        setMaskedByte2FromFlag(8, hasBasePlate);
        put(MetadataItem.of((byte) CLIENT_FLAGS_INDEX, maskedByte2));
    }

    public void setMarker(boolean marker) {
        setMaskedByte2FromFlag( 16, marker);
        put(MetadataItem.of((byte) CLIENT_FLAGS_INDEX, maskedByte2));
    }

    public void setHeadPose(Vector3Df vector3Df) {
        Preconditions.checkNotNull(vector3Df, "Vector is null!");
        this.headPose = vector3Df;
        put(MetadataItem.of((byte) HEAD_POSE_INDEX, this.headPose));
    }

    private void setMaskedByte2FromFlag(int i, boolean flag) {
        if (flag) {
            maskedByte2 = (byte) (maskedByte2 | i);
        } else {
            maskedByte2 = (byte)(maskedByte2 & (~i));
        }
    }

    public void setMaskedByte1FromFlag(int index, boolean value) {
        if (value) {
            maskedByte = (byte) (maskedByte | 1 << index);
        } else {
            maskedByte = (byte) (maskedByte & (~(1 << index)));
        }
    }

    public int getTypeId() {
        return ClassStorage.getEntityTypeId("armor_stand", ArmorStandAccessor.getType());
    }
}
