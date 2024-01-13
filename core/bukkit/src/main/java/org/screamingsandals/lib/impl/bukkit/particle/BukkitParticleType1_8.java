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

package org.screamingsandals.lib.impl.bukkit.particle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.impl.nms.accessors.EnumParticleAccessor;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.particle.ParticleType;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;

public class BukkitParticleType1_8 extends BasicWrapper<Integer> implements ParticleType {
    public BukkitParticleType1_8(int particleId) {
        super(particleId);
    }

    @Override
    public @NotNull String platformName() {
        return String.valueOf(wrappedObject);
    }

    @Override
    public @Nullable Class<? extends ParticleData> expectedDataClass() {
        if (wrappedObject == 37 || wrappedObject == 38) {
            return Block.class;
        } else if (wrappedObject == 36) {
            return ItemStack.class;
        }
        return null;
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof Integer || object instanceof ParticleType) {
            return equals(object);
        }
        return equals(ParticleType.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return ResourceLocation.of(BukkitParticleTypeRegistry1_8.ORDINAL_TO_PARTICLE_NAME.get(wrappedObject));
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == EnumParticleAccessor.getType()) {
            //noinspection unchecked
            return (T) Reflect.fastInvoke(EnumParticleAccessor.getMethodA1(), wrappedObject);
        }
        return super.as(type);
    }
}
