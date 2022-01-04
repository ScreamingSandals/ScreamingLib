/*
 * Copyright 2022 ScreamingSandals
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

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface ParticleTypeHolder extends ComparableWrapper, RawValueHolder {

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    String platformName();

    @Nullable
    Class<? extends ParticleData> expectedDataClass();

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
        return ofOptional(particle).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.PARTICLE_TYPE)
    static Optional<ParticleTypeHolder> ofOptional(Object particle) {
        if (particle instanceof ParticleTypeHolder) {
            return Optional.of((ParticleTypeHolder) particle);
        }
        return ParticleTypeMapping.resolve(particle);
    }

    static List<ParticleTypeHolder> all() {
        return ParticleTypeMapping.getValues();
    }
}
