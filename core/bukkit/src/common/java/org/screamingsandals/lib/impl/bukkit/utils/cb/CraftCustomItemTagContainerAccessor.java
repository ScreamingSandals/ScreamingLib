package org.screamingsandals.lib.impl.bukkit.utils.cb;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.ext.takenaka.util.LazySupplier;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Supplier;

public interface CraftCustomItemTagContainerAccessor {
    @NotNull Supplier<@Nullable Class<?>> TYPE = LazySupplier.of(() -> Reflect.getClassSafe(ClassStorage.CB_PACKAGE + ".inventory.tags.CraftCustomItemTagContainer"));

    @NotNull Supplier<@Nullable Method> METHOD_PUT_ALL = LazySupplier.of(() -> Reflect.getReflectMethod(TYPE.get(), "putAll", Map.class));
    @NotNull Supplier<@Nullable Method> METHOD_GET_RAW = LazySupplier.of(() -> Reflect.getReflectMethod(TYPE.get(), "getRaw"));
}
