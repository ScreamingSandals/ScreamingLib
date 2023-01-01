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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;

@SuppressWarnings("AlternativeMethodAvailable")
public interface ParticleTypeHolder extends ComparableWrapper, RawValueHolder {

    String platformName();

    @Nullable Class<? extends ParticleData> expectedDataClass();

    /**
     * {@inheritDoc}
     */
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    boolean is(Object object);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    static ParticleTypeHolder of(Object particle) {
        var result = ofNullable(particle);
        Preconditions.checkNotNullIllegal(result, "Could not find particle type: " + particle);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    @Contract("null -> null")
    static @Nullable ParticleTypeHolder ofNullable(@Nullable Object particle) {
        if (particle instanceof ParticleTypeHolder) {
            return (ParticleTypeHolder) particle;
        }
        return ParticleTypeMapping.resolve(particle);
    }

    static @NotNull List<@NotNull ParticleTypeHolder> all() {
        return ParticleTypeMapping.getValues();
    }
}
