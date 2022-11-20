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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;

@SuppressWarnings("AlternativeMethodAvailable")
public interface EquipmentSlotHolder extends ComparableWrapper, RawValueHolder {

    String platformName();

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    static @NotNull EquipmentSlotHolder of(@NotNull Object slot) {
        var result = ofNullable(slot);
        Preconditions.checkNotNullIllegal(result, "Could not find equipment slot: " + slot);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Contract("null -> null")
    static @Nullable EquipmentSlotHolder ofNullable(@Nullable Object slot) {
        if (slot instanceof EquipmentSlotHolder) {
            return (EquipmentSlotHolder) slot;
        }
        return EquipmentSlotMapping.resolve(slot);
    }

    static @NotNull List<@NotNull EquipmentSlotHolder> all() {
        return EquipmentSlotMapping.getValues();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Override
    boolean is(Object... objects);
}
