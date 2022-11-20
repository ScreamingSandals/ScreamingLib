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

package org.screamingsandals.lib.bukkit.particle;

import org.bukkit.Particle;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.particle.*;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitParticleTypeHolder extends BasicWrapper<Particle> implements ParticleTypeHolder {
    public BukkitParticleTypeHolder(Particle wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public @Nullable Class<? extends ParticleData> expectedDataClass() {
        var dataType = wrappedObject.getDataType();
        if (dataType != Void.class) {
            switch (dataType.getSimpleName()) {
                case "MaterialData":
                case "BlockData":
                    return BlockTypeHolder.class;
                case "ItemStack":
                    return Item.class;
                case "DustOptions":
                    return DustOptions.class;
                case "DustTransition":
                    return DustTransition.class;
            }
        }
        return null;
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Particle || object instanceof ParticleTypeHolder) {
            return equals(object);
        }
        return equals(ParticleTypeHolder.ofNullable(object));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
