/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.compat.datafixer2;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.nbt.NbtOpsAccessor;
import org.screamingsandals.lib.impl.nms.accessors.server.MinecraftServerAccessor;
import org.screamingsandals.lib.impl.nms.accessors.util.datafix.fixes.ReferencesAccessor;
import org.screamingsandals.lib.utils.reflect.Reflect;

/**
 * Serialization stuff used to be part of the datafixers package in earlier versions.
 */
@UtilityClass
public class DataFixer2Compat {
    @SuppressWarnings("unchecked")
    public static @NotNull Object dataFixItemStack(@NotNull Object vanilla, int dataVersion, int currentVersion) {
        var fixerUpper = (DataFixer) Reflect.getField(ClassStorage.getMinecraftServerObject(), MinecraftServerAccessor.FIELD_FIXER_UPPER.get());
        return fixerUpper
                .update((DSL.TypeReference) ReferencesAccessor.CONST_ITEM_STACK.get(), new Dynamic<>((DynamicOps<Object>) NbtOpsAccessor.CONST_INSTANCE.get(), vanilla), dataVersion, currentVersion)
                .getValue();
    }
}
