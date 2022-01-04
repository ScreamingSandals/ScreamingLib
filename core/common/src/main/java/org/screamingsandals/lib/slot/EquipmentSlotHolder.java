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

package org.screamingsandals.lib.slot;

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface EquipmentSlotHolder extends ComparableWrapper, RawValueHolder {

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    String platformName();

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    static EquipmentSlotHolder of(Object slot) {
        return ofOptional(slot).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    static Optional<EquipmentSlotHolder> ofOptional(Object slot) {
        if (slot instanceof EquipmentSlotHolder) {
            return Optional.of((EquipmentSlotHolder) slot);
        }
        return EquipmentSlotMapping.resolve(slot);
    }

    static List<EquipmentSlotHolder> all() {
        return EquipmentSlotMapping.getValues();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Override
    boolean is(Object... objects);
}
