package org.screamingsandals.lib.bukkit.hologram.nms;
import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.nms.accessors.ArmorStandAccessor;
import org.screamingsandals.lib.nms.accessors.EntityDataAccessorAccessor;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class FakeArmorStandNMS extends FakeEntityNMS {
    public static final int CLIENT_FLAGS_INDEX;
    public static final int HEAD_POSE_INDEX;

    static {
        final var object = Reflect.getField(ArmorStandAccessor.getFieldDATA_CLIENT_FLAGS());
        CLIENT_FLAGS_INDEX = (int) Reflect.fastInvoke(object, EntityDataAccessorAccessor.getMethodGetId1());

        final var object2 = Reflect.getField(ArmorStandAccessor.getFieldDATA_HEAD_POSE());
        HEAD_POSE_INDEX = (int) Reflect.fastInvoke(object2, EntityDataAccessorAccessor.getMethodGetId1());
    }

    private byte maskedByte = 0;
    private Vector3Df headPose;

    public FakeArmorStandNMS(Location location) {
        super(location);
        headPose = new Vector3Df(0, 0, 0);
        dataWatcher.register(DataWatcher.Item.of(CLIENT_FLAGS_INDEX, maskedByte));
    }

    public void setSmall(boolean isSmall) {
        maskedByte = getMaskedByteFromBoolFlag(maskedByte, 1, isSmall);
        dataWatcher.register(DataWatcher.Item.of(CLIENT_FLAGS_INDEX, maskedByte));
    }

    @Override
    public void setGravity(boolean gravity) {
        maskedByte = getMaskedByteFromBoolFlag(maskedByte, 2, gravity);
        dataWatcher.register(DataWatcher.Item.of(CLIENT_FLAGS_INDEX, maskedByte));
    }

    public void setArms(boolean hasArms) {
        maskedByte = getMaskedByteFromBoolFlag(maskedByte, 4, hasArms);
        dataWatcher.register(DataWatcher.Item.of(CLIENT_FLAGS_INDEX, maskedByte));
    }

    public void setBasePlate(boolean hasBasePlate) {
        maskedByte = getMaskedByteFromBoolFlag(maskedByte, 8, hasBasePlate);
        dataWatcher.register(DataWatcher.Item.of(CLIENT_FLAGS_INDEX, maskedByte));
    }

    public void setMarker(boolean marker) {
        maskedByte = getMaskedByteFromBoolFlag(maskedByte, 16, marker);
        dataWatcher.register(DataWatcher.Item.of(CLIENT_FLAGS_INDEX, maskedByte));
    }

    public void setRotation(Vector3Df vector3Df) {
        Preconditions.checkNotNull(vector3Df, "Vector is null!");
        this.headPose = vector3Df;
        final var nmsVector = ClassStorage.getVectorToNMS(vector3Df);
        dataWatcher.register(DataWatcher.Item.of(HEAD_POSE_INDEX, nmsVector));
    }

    public Vector3Df getRotation() {
        return headPose;
    }

    private byte getMaskedByteFromBoolFlag(byte b, int i, boolean flag) {
        if (flag) {
            b = (byte) (b | i);
        } else {
            b = (byte) (b & (~i));
        }
        return b;
    }

    //TODO: misat pls do this xdd
    public int getTypeId() {
        return ClassStorage.getEntityTypeId("armor_stand", ClassStorage.NMS.EntityArmorStand);
    }
}
