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

package org.screamingsandals.lib.particle;

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.impl.particle.ParticleTypeRegistry;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface ParticleType extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @Nullable Class<? extends ParticleData> expectedDataClass();

    /**
     * {@inheritDoc}
     */
    @Override
    boolean is(@MinecraftType(MinecraftType.Type.PARTICLE_TYPE) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.PARTICLE_TYPE) @Nullable Object @NotNull... objects);

    static @NotNull ParticleType of(@MinecraftType(MinecraftType.Type.PARTICLE_TYPE) @NotNull Object particle) {
        var result = ofNullable(particle);
        Preconditions.checkNotNullIllegal(result, "Could not find particle type: " + particle);
        return result;
    }

    @Contract("null -> null")
    static @Nullable ParticleType ofNullable(@MinecraftType(MinecraftType.Type.PARTICLE_TYPE) @Nullable Object particle) {
        if (particle instanceof ParticleType) {
            return (ParticleType) particle;
        }
        return ParticleTypeRegistry.getInstance().resolveMapping(particle);
    }

    static @NotNull RegistryItemStream<@NotNull ParticleType> all() {
        return ParticleTypeRegistry.getInstance().getRegistryItemStream();
    }
}
