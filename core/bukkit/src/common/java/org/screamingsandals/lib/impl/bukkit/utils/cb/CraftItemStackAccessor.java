/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.bukkit.utils.cb;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.ext.takenaka.util.LazySupplier;
import org.screamingsandals.lib.impl.nms.accessors.world.item.ItemStackAccessor;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public interface CraftItemStackAccessor {
    @NotNull Supplier<@Nullable Class<?>> TYPE = LazySupplier.of(() -> Reflect.getClassSafe(ClassStorage.CB_PACKAGE + ".inventory.CraftItemStack"));

    @NotNull Supplier<@Nullable Field> FIELD_HANDLE = LazySupplier.of(() -> Reflect.getReflectField(TYPE.get(), "handle"));

    @NotNull Supplier<@Nullable Method> METHOD_AS_NMS_COPY = LazySupplier.of(() -> Reflect.getReflectMethod(TYPE.get(), "asNMSCopy", ItemStack.class));
    @NotNull Supplier<@Nullable Method> METHOD_AS_CRAFT_COPY = LazySupplier.of(() -> Reflect.getReflectMethod(TYPE.get(), "asCraftCopy", ItemStack.class));
    @NotNull Supplier<@Nullable Method> METHOD_AS_CRAFT_MIRROR = LazySupplier.of(() -> Reflect.getReflectMethod(TYPE.get(), "asCraftMirror", ItemStackAccessor.TYPE.get()));

    static @Nullable Object getHandleOfItemStack(@NotNull Object obj) {
        return Reflect.getField(obj, FIELD_HANDLE.get());
    }

    static @NotNull Object stackAsNMS(@NotNull ItemStack item) {
        Preconditions.checkNotNull(item, "Item is null!");
        return Reflect.fastInvoke(METHOD_AS_NMS_COPY.get(), item);
    }

    static @NotNull ItemStack asCBStack(@NotNull ItemStack item) {
        Preconditions.checkNotNull(item, "Item is null!");
        return (ItemStack) Reflect.fastInvoke(METHOD_AS_CRAFT_COPY.get(), item);
    }

    static @NotNull ItemStack nmsAsStack(@NotNull Object nmsStack) {
        return (ItemStack) Reflect.fastInvoke(METHOD_AS_CRAFT_MIRROR.get(), nmsStack);
    }
}
