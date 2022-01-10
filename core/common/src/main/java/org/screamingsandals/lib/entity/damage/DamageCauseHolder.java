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

package org.screamingsandals.lib.entity.damage;

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface DamageCauseHolder extends ComparableWrapper, RawValueHolder {

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    String platformName();

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    boolean is(Object damageCause);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    boolean is(Object... damageCauses);

    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    static DamageCauseHolder of(Object damageCause) {
        return ofOptional(damageCause).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.DAMAGE_CAUSE)
    static Optional<DamageCauseHolder> ofOptional(Object damageCause) {
        if (damageCause instanceof DamageCauseHolder) {
            return Optional.of((DamageCauseHolder) damageCause);
        }
        return DamageCauseMapping.resolve(damageCause);
    }

    static List<DamageCauseHolder> all() {
        return DamageCauseMapping.getValues();
    }
}
