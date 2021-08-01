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

    private final int id = EntityNMS.incrementAndGetId();
    private LocationHolder location;
    private UUID uuid = UUID.randomUUID();
    private final List<MetadataItem> metadataItems = new ArrayList<>();
    private byte maskedByte = 0;
    private byte maskedByte2 = 0;
    private Component customName = Component.empty();
    private Vector3Df headPose;

    public HologramPiece(LocationHolder location) {
        this.location = location;
        this.headPose = new Vector3Df(0.0f, 0.0f, 0.0f);

        put(MetadataItem.of((byte) 0, (byte) 0));
        put(MetadataItem.of((byte) 1, 300));
        if (Version.isVersion(1, 13)) {
            put(MetadataItem.ofOpt((byte) 2, Component.empty()));
        } else {
            put(MetadataItem.of((byte) 2, " "));
        }
        put(MetadataItem.of((byte) 3, false));
        put(MetadataItem.of((byte) 4, false));
    }

    public void put(MetadataItem metadataItem) {
        metadataItems.removeIf(metadataItem1 -> metadataItem1.getIndex() == metadataItem.getIndex());
        metadataItems.add(metadataItem);
    }

    public void setInvisible(boolean invisible) {
        maskedByte = getMaskedByteFromBoolFlag(maskedByte, 5, invisible);
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
        maskedByte2 = getMaskedByteFromBoolFlag(maskedByte2, 1, isSmall);
        put(MetadataItem.of((byte) CLIENT_FLAGS_INDEX, maskedByte));
    }

    public void setGravity(boolean gravity) {
        maskedByte2 = getMaskedByteFromBoolFlag(maskedByte2, 2, gravity);
        put(MetadataItem.of((byte) CLIENT_FLAGS_INDEX, maskedByte2));
    }

    public void setArms(boolean hasArms) {
        maskedByte2 = getMaskedByteFromBoolFlag(maskedByte2, 4, hasArms);
        put(MetadataItem.of((byte) CLIENT_FLAGS_INDEX, maskedByte2));
    }

    public void setBasePlate(boolean hasBasePlate) {
        maskedByte2 = getMaskedByteFromBoolFlag(maskedByte2, 8, hasBasePlate);
        put(MetadataItem.of((byte) CLIENT_FLAGS_INDEX, maskedByte2));
    }

    public void setMarker(boolean marker) {
        maskedByte2 = getMaskedByteFromBoolFlag(maskedByte2, 16, marker);
        put(MetadataItem.of((byte) CLIENT_FLAGS_INDEX, maskedByte2));
    }

    public void setHeadPose(Vector3Df vector3Df) {
        Preconditions.checkNotNull(vector3Df, "Vector is null!");
        this.headPose = vector3Df;
        put(MetadataItem.of((byte) HEAD_POSE_INDEX, this.headPose));
    }

    private byte getMaskedByteFromBoolFlag(byte b, int i, boolean flag) {
        if (flag) {
            return (byte)(b | 1 << i);
        } else {
            return (byte)(b & (~(1 << i)));
        }
    }

    public int getTypeId() {
        return ClassStorage.getEntityTypeId("armor_stand", ArmorStandAccessor.getType());
    }
}
