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

package org.screamingsandals.lib.impl.bukkit.utils;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.core.HolderLookup$ProviderAccessor;
import org.screamingsandals.lib.impl.nms.accessors.core.RegistryAccessAccessor;
import org.screamingsandals.lib.impl.nms.accessors.nbt.CompoundTagAccessor;
import org.screamingsandals.lib.impl.nms.accessors.nbt.NbtOpsAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.chat.ComponentSerializationAccessor;
import org.screamingsandals.lib.impl.nms.accessors.server.MinecraftServerAccessor;
import org.screamingsandals.lib.impl.nms.accessors.util.datafix.fixes.ReferencesAccessor;
import org.screamingsandals.lib.impl.nms.accessors.world.item.ItemStackAccessor;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.io.StringReader;
import java.util.Optional;

@UtilityClass
public class DataFixerUtils {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static @NotNull Optional<?> parseItemStack(@NotNull Object compound) {
        var codecRes = ((Codec) ItemStackAccessor.CONST_CODEC.get()).parse(
                (DynamicOps) Reflect.fastInvoke(
                        Reflect.fastInvoke(ClassStorage.getMinecraftServerObject(), MinecraftServerAccessor.METHOD_REGISTRY_ACCESS.get()),
                        HolderLookup$ProviderAccessor.METHOD_CREATE_SERIALIZATION_CONTEXT.get(),
                        NbtOpsAccessor.CONST_INSTANCE.get()
                ),
                compound
        );
        return codecRes.resultOrPartial(string -> Bukkit.getLogger().warning("Tried to load invalid item: '" + string + "'"));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static @NotNull Object encodeItemStack(@NotNull Object nmsStack) {
        return ((Codec) ItemStackAccessor.CONST_CODEC.get()).encode(
                nmsStack,
                (DynamicOps) Reflect.fastInvoke(
                        Reflect.fastInvoke(ClassStorage.getMinecraftServerObject(), MinecraftServerAccessor.METHOD_REGISTRY_ACCESS.get()),
                        HolderLookup$ProviderAccessor.METHOD_CREATE_SERIALIZATION_CONTEXT.get(),
                        NbtOpsAccessor.CONST_INSTANCE.get()
                ),
                Reflect.construct(CompoundTagAccessor.CONSTRUCTOR_0.get())
        ).getOrThrow();
    }

    @SuppressWarnings("unchecked")
    public static @NotNull Object dataFixItemStack(@NotNull Object vanilla, int dataVersion, int currentVersion) {
        var fixerUpper = (DataFixer) Reflect.getField(ClassStorage.getMinecraftServerObject(), MinecraftServerAccessor.FIELD_FIXER_UPPER.get());
        return fixerUpper
                .update((DSL.TypeReference) ReferencesAccessor.CONST_ITEM_STACK.get(), new Dynamic<>((DynamicOps<Object>) NbtOpsAccessor.CONST_INSTANCE.get(), vanilla), dataVersion, currentVersion)
                .getValue();
    }

    @SuppressWarnings({"unchecked", "rawtypes", "deprecation"})
    public static @NotNull Object parseComponent(@NotNull String javaJson) {
        var reader = new JsonReader(new StringReader(javaJson));
        reader.setLenient(true);
        var element = JsonParser.parseReader(reader);

        return ((Codec) ComponentSerializationAccessor.CONST_CODEC.get()).parse(
                (DynamicOps<?>) Reflect.fastInvoke(
                        RegistryAccessAccessor.CONST_EMPTY.get(),
                        HolderLookup$ProviderAccessor.METHOD_CREATE_SERIALIZATION_CONTEXT.get(),
                        JsonOps.INSTANCE
                ),
                element
        ).getOrThrow();
    }
}
