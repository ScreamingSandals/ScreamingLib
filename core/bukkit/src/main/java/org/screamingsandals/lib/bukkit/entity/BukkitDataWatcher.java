package org.screamingsandals.lib.bukkit.entity;
import org.bukkit.entity.Entity;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.nms.accessors.EntityAccessor;
import org.screamingsandals.lib.nms.accessors.EntityDataAccessorAccessor;
import org.screamingsandals.lib.nms.accessors.EntityDataSerializersAccessor;
import org.screamingsandals.lib.nms.accessors.SynchedEntityDataAccessor;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitDataWatcher extends DataWatcher {
    private final Object dataWatcher;

    public static final Object BYTE_SERIALIZER;
    public static final Object INTEGER_SERIALIZER;
    public static final Object FLOAT_SERIALIZER;
    public static final Object STRING_SERIALIZER;
    public static final Object BOOLEAN_SERIALIZER;
    public static final Object VECTOR3DF_SERIALIZER;

    private static Object getNewDataWatcherObject() {
        return Reflect.construct(SynchedEntityDataAccessor.getConstructor0(), (Object) null);
    }

    static {
        BYTE_SERIALIZER = Reflect.getField(ClassStorage.NMS.DataWatcherRegistry, EntityDataSerializersAccessor.getFieldBYTE());
        INTEGER_SERIALIZER = Reflect.getField(ClassStorage.NMS.DataWatcherRegistry, EntityDataSerializersAccessor.getFieldINT());
        FLOAT_SERIALIZER = Reflect.getField(ClassStorage.NMS.DataWatcherRegistry, EntityDataSerializersAccessor.getFieldFLOAT());
        STRING_SERIALIZER = Reflect.getField(ClassStorage.NMS.DataWatcherRegistry, EntityDataSerializersAccessor.getFieldSTRING());
        BOOLEAN_SERIALIZER = Reflect.getField(ClassStorage.NMS.DataWatcherRegistry, EntityDataSerializersAccessor.getFieldBOOLEAN());
        VECTOR3DF_SERIALIZER = Reflect.getField(ClassStorage.NMS.DataWatcherRegistry, EntityDataSerializersAccessor.getFieldROTATIONS());
    }

    public static Object getSerializerOfType(Object query) {
        if (query == null) {
            throw new UnsupportedOperationException("Query cannot be null!");
        }

        if (query instanceof Byte) {
            return BYTE_SERIALIZER;
        }

        else if (query instanceof Integer) {
            return INTEGER_SERIALIZER;
        }

        else if (query instanceof Float) {
            return FLOAT_SERIALIZER;
        }

        else if (query instanceof String) {
            return STRING_SERIALIZER;
        }

        else if (query instanceof Boolean) {
            return BOOLEAN_SERIALIZER;
        }

        else if (ClassStorage.NMS.Vector3f.isInstance(query)) {
            return VECTOR3DF_SERIALIZER;
        }

        return BYTE_SERIALIZER;
    }

    public BukkitDataWatcher(EntityBasic entity) {
        if (entity == null) {
            dataWatcher = getNewDataWatcherObject();
            return;
        }
        dataWatcher = ClassStorage.getDataWatcher(ClassStorage.getHandle(entity.as(Entity.class)));
    }

    public BukkitDataWatcher(Object object) {
        if (object == null) {
            object = getNewDataWatcherObject();
        }
        dataWatcher = object;
    }

    @Override
    public <T> void register(DataWatcher.Item<T> item) {
        Reflect.fastInvoke(dataWatcher, SynchedEntityDataAccessor.getMethodCreateDataItem1(), getDataWatcherObject(item.getIndex(), getSerializerOfType(item.getValue())), item.getValue());
    }

    @Override
    public <T> void set(Item<T> item) {
        Reflect.fastInvoke(dataWatcher, SynchedEntityDataAccessor.getMethodSet1(), getDataWatcherObject(item.getIndex(), getSerializerOfType(item.getValue())), item.getValue());
    }

    @Override
    public Object get(int index, Object serializer) {
        return Reflect.fastInvoke(dataWatcher, SynchedEntityDataAccessor.getMethodGet1(), getDataWatcherObject(index, serializer));
    }

    protected Object getDataWatcherObject(int index, Object serializer) {
        if (serializer == null) {
            throw new UnsupportedOperationException("Invalid serializer provided!");
        }

        return Reflect.construct(EntityDataAccessorAccessor.getConstructor0(), index, serializer);
    }

    public Object toNMS() {
        return dataWatcher;
    }
}
