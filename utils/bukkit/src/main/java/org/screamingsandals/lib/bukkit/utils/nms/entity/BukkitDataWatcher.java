package org.screamingsandals.lib.bukkit.utils.nms.entity;

import org.bukkit.entity.Entity;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.entity.DataWatcher;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitDataWatcher extends DataWatcher {
    private final Object dataWatcher;

    public BukkitDataWatcher(Object dataWatcher) {
        this.dataWatcher = dataWatcher;
    }

    public BukkitDataWatcher(EntityBasic entity) {
        dataWatcher = ClassStorage.getDataWatcher(ClassStorage.getHandle(entity.as(Entity.class)));
    }

    @Override
    public <T> void register(DataWatcher.Item<T> item) {
        Reflect.getMethod(dataWatcher, "register",  ClassStorage.NMS.DataWatcherObject, Object.class)
                .invoke(getDataWatcherObject(item.getIndex()), item.getValue());
    }

    @Override
    public <T> void set(Item<T> item) {
        Reflect.getMethod(dataWatcher, "set",  ClassStorage.NMS.DataWatcherObject, Object.class)
                .invoke(getDataWatcherObject(item.getIndex()), item.getValue());
    }

    protected Object getDataWatcherObject(int index) {
        final var dataWatcherSerializer = Reflect.getField(ClassStorage.NMS.DataWatcherRegistry, "a");
        return Reflect.constructor(ClassStorage.NMS.DataWatcherObject, int.class, ClassStorage.NMS.DataWatcherSerializer)
                .construct(index, dataWatcherSerializer);
    }

    public Object toNMS() {
        return dataWatcher;
    }
}
