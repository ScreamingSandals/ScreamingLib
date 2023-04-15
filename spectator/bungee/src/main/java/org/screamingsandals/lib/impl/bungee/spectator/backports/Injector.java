/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.impl.bungee.spectator.backports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Excluder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.reflect.Reflect;

// based on: https://github.com/KyoriPowered/adventure-platform/blob/d867ccbc2aed826d72ceac9ff33108e42d55900e/text-serializer-bungeecord/src/main/java/net/kyori/adventure/text/serializer/bungeecord/GsonInjections.java
@UtilityClass
@ApiStatus.Internal
public class Injector {
    @SuppressWarnings("unchecked")
    public static boolean injectGson(final @NotNull Gson existing, final @NotNull Consumer<@NotNull GsonBuilder> accepter) {
        try {
            final var builder = new GsonBuilder();
            accepter.accept(builder);

            final var existingFactories = (List<TypeAdapterFactory>) Reflect.getField(existing, "factories");
            final var newFactories = new ArrayList<>((List<TypeAdapterFactory>) Reflect.getField(builder, "factories"));
            Collections.reverse(newFactories);
            newFactories.addAll((List<TypeAdapterFactory>) Reflect.getField(builder, "hierarchyFactories"));

            final var modifiedFactories = new ArrayList<>(existingFactories);

            final int index = findExcluderIndex(modifiedFactories);

            Collections.reverse(newFactories);
            for (final var newFactory : newFactories) {
                modifiedFactories.add(index, newFactory);
            }

            Reflect.setField(existing, "factories", modifiedFactories);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static int findExcluderIndex(final @NotNull List<TypeAdapterFactory> factories) {
        for (int i = 0, size = factories.size(); i < size; i++) {
            final var factory = factories.get(i);
            if (factory instanceof Excluder) {
                return i + 1;
            }
        }
        return 0;
    }
}
