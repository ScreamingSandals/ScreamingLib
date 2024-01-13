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

package org.screamingsandals.lib.entity.damage;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.TaggableHolder;
import org.screamingsandals.lib.impl.entity.damage.DamageTypeRegistry;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public interface DamageType extends RegistryItem, RawValueHolder, TaggableHolder {

    @ApiStatus.Experimental
    @NotNull String platformName();

    /**
     * This method accept any object that represents damage type, or tags if prefixed with #
     *
     * @param object object that represents damage type
     * @return true if this damage type is equivalent to the object
     */
    @Override
    boolean is(@MinecraftType(MinecraftType.Type.DAMAGE_TYPE) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.DAMAGE_TYPE) @Nullable Object @NotNull... objects);

    static @NotNull DamageType of(@MinecraftType(MinecraftType.Type.DAMAGE_TYPE) @NotNull Object damageType) {
        var result = ofNullable(damageType);
        Preconditions.checkNotNullIllegal(result, "Could not find damage type: " + damageType);
        return result;
    }

    @Contract("null -> null")
    static @Nullable DamageType ofNullable(@MinecraftType(MinecraftType.Type.DAMAGE_TYPE) @Nullable Object damageType) {
        if (damageType instanceof DamageType) {
            return (DamageType) damageType;
        }
        return DamageTypeRegistry.getInstance().resolveMapping(damageType);
    }

    static @NotNull RegistryItemStream<@NotNull DamageType> all() {
        return DamageTypeRegistry.getInstance().getRegistryItemStream();
    }
}
