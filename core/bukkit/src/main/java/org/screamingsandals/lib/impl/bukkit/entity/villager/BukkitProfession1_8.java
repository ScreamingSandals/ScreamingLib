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

package org.screamingsandals.lib.impl.bukkit.entity.villager;

import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.villager.Profession;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitProfession1_8 extends BasicWrapper<Villager.Profession> implements Profession {

    public BukkitProfession1_8(@NotNull Villager.Profession wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof Villager.Profession || object instanceof Profession) {
            return equals(object);
        }
        return equals(Profession.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        if ("BLACKSMITH".equals(wrappedObject.name())) {
            return ResourceLocation.of("minecraft", "toolsmith");
        } else if ("PRIEST".equals(wrappedObject.name())) {
            return ResourceLocation.of("minecraft", "cleric");
        }
        return ResourceLocation.of(wrappedObject.name());
    }
}
