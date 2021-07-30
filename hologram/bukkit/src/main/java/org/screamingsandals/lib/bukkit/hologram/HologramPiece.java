package org.screamingsandals.lib.bukkit.hologram;

import com.google.common.base.Preconditions;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.hologram.nms.FakeArmorStandNMS;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.entity.EntityNMS;
import org.screamingsandals.lib.nms.accessors.ArmorStandAccessor;
import org.screamingsandals.lib.packet.MetadataItem;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class HologramPiece {
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
        put(MetadataItem.of((byte) 2, " "));
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
        var str = AdventureHelper.toLegacy(name);
        if (str.length() > 256) {
            str = str.substring(0, 256);
        }
        put(MetadataItem.of((byte) 2, str));
        customName = name;
    }

    public void setSmall(boolean isSmall) {
        maskedByte2 = getMaskedByteFromBoolFlag(maskedByte2, 1, isSmall);
        put(MetadataItem.of((byte) FakeArmorStandNMS.CLIENT_FLAGS_INDEX, maskedByte));
    }

    public void setGravity(boolean gravity) {
        maskedByte2 = getMaskedByteFromBoolFlag(maskedByte2, 2, gravity);
        put(MetadataItem.of((byte) FakeArmorStandNMS.CLIENT_FLAGS_INDEX, maskedByte));
    }

    public void setArms(boolean hasArms) {
        maskedByte2 = getMaskedByteFromBoolFlag(maskedByte2, 4, hasArms);
        put(MetadataItem.of((byte) FakeArmorStandNMS.CLIENT_FLAGS_INDEX, maskedByte));
    }

    public void setBasePlate(boolean hasBasePlate) {
        maskedByte2 = getMaskedByteFromBoolFlag(maskedByte2, 8, hasBasePlate);
        put(MetadataItem.of((byte) FakeArmorStandNMS.CLIENT_FLAGS_INDEX, maskedByte));
    }

    public void setMarker(boolean marker) {
        maskedByte2 = getMaskedByteFromBoolFlag(maskedByte2, 16, marker);
        put(MetadataItem.of((byte) FakeArmorStandNMS.CLIENT_FLAGS_INDEX, maskedByte));
    }

    public void setHeadPose(Vector3Df vector3Df) {
        Preconditions.checkNotNull(vector3Df, "Vector is null!");
        this.headPose = vector3Df;
        put(MetadataItem.of((byte) FakeArmorStandNMS.HEAD_POSE_INDEX, this.headPose));
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
