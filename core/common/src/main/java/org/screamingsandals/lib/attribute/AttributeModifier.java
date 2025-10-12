/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.attribute;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.impl.attribute.AttributeModifierIds;
import org.screamingsandals.lib.impl.attribute.Attributes;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.UUID;

@Data
public class AttributeModifier implements Wrapper {
    private final @NotNull ResourceLocation location;
    @Getter(onMethod_ = {@ApiStatus.Obsolete, @LimitedVersionSupport("<= 1.20.6; most likely contains null on 1.21+")})
    private final @Nullable UUID uuid;
    @Getter(onMethod_ = {@ApiStatus.Obsolete, @LimitedVersionSupport("<= 1.20.6; most likely contains null on 1.21+")})
    private final @Nullable String name;
    private final double amount;
    private final @NotNull Operation operation;

    public AttributeModifier(@NotNull ResourceLocation location, double amount, @NotNull Operation operation) {
        this.location = location;
        if (!Server.isVersion(1, 21)) {
            var downgrade = AttributeModifierIds.downgradeResourceLocation(location);
            this.uuid = downgrade.first();
            this.name = downgrade.second();
        } else {
            this.uuid = null;
            this.name = null;
        }
        this.amount = amount;
        this.operation = operation;
    }

    public AttributeModifier(@NotNull UUID uuid, @NotNull String name, double amount, @NotNull Operation operation) {
        this.location = AttributeModifierIds.getResourceLocation(uuid, name);
        this.uuid = uuid;
        this.name = name;
        this.amount = amount;
        this.operation = operation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        return Attributes.convertAttributeModifierHolder(this, type);
    }

    public enum Operation {
        ADD_VALUE,
        ADD_MULTIPLIED_BASE,
        ADD_MULTIPLIED_TOTAL;

        public static @NotNull Operation byOrdinal(int ordinal) {
            return values()[ordinal];
        }
    }
}
