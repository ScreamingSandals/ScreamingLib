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

package org.screamingsandals.lib.slot;

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface EquipmentSlot extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Override
    boolean is(@Nullable Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Override
    boolean is(@Nullable Object @NotNull... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    static @NotNull EquipmentSlot of(@NotNull Object slot) {
        var result = ofNullable(slot);
        Preconditions.checkNotNullIllegal(result, "Could not find equipment slot: " + slot);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.EQUIPMENT_SLOT)
    @Contract("null -> null")
    static @Nullable EquipmentSlot ofNullable(@Nullable Object slot) {
        if (slot instanceof EquipmentSlot) {
            return (EquipmentSlot) slot;
        }
        return EquipmentSlotRegistry.getInstance().resolveMapping(slot);
    }

    static @NotNull RegistryItemStream<@NotNull EquipmentSlot> all() {
        return EquipmentSlotRegistry.getInstance().getRegistryItemStream();
    }
}
