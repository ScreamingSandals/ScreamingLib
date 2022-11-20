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

package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitPotionHolder extends BasicWrapper<PotionData> implements PotionHolder {

    public BukkitPotionHolder(PotionType type) {
        this(new PotionData(type));
    }

    public BukkitPotionHolder(PotionData wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        if (wrappedObject.isExtended()) {
            return "LONG_" + wrappedObject.getType().name();
        } else if (wrappedObject.isUpgraded()) {
            return "STRONG_" + wrappedObject.getType().name();
        }
        return wrappedObject.getType().name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof PotionData || object instanceof PotionHolder) {
            return equals(object);
        }
        return equals(PotionHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == PotionType.class) {
            return (T) wrappedObject.getType();
        }
        return super.as(type);
    }
}
