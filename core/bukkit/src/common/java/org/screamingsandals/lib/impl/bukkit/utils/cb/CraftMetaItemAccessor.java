package org.screamingsandals.lib.impl.bukkit.utils.cb;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.ext.takenaka.util.LazySupplier;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public interface CraftMetaItemAccessor {
    @NotNull Supplier<@Nullable Class<?>> TYPE = LazySupplier.of(() -> Reflect.getClassSafe(ClassStorage.CB_PACKAGE + ".inventory.CraftMetaItem"));

    @NotNull Supplier<@Nullable Field> FIELD_UNHANDLED_TAGS = LazySupplier.of(() -> Reflect.getReflectField(TYPE.get(), "unhandledTags"));
}
