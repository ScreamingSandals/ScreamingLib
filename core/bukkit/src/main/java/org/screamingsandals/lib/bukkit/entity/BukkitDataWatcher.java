package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.Entity;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitDataWatcher extends DataWatcher {
    private final Object dataWatcher;

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

    private static Object getNewDataWatcherObject() {
        return Reflect.constructor(ClassStorage.NMS.DataWatcher, ClassStorage.NMS.Entity).construct((Object) null);
    }

    @Override
    public <T> void register(DataWatcher.Item<T> item) {
        Reflect.getMethod(dataWatcher, "register,func_187214_a1",  ClassStorage.NMS.DataWatcherObject, Object.class)
                .invoke(getDataWatcherObject(item.getIndex()), item.getValue());
    }

    @Override
    public <T> void set(Item<T> item) {
        Reflect.getMethod(dataWatcher, "set,func_187227_b",  ClassStorage.NMS.DataWatcherObject, Object.class)
                .invoke(getDataWatcherObject(item.getIndex()), item.getValue());
    }

    protected Object getDataWatcherObject(int index) {
        final var dataWatcherSerializer = Reflect.getField(ClassStorage.NMS.DataWatcherRegistry, "a,field_187191_a");
        return Reflect.constructor(ClassStorage.NMS.DataWatcherObject, int.class, ClassStorage.NMS.DataWatcherSerializer)
                .construct(index, dataWatcherSerializer);
    }

    public Object toNMS() {
        return dataWatcher;
    }
}
