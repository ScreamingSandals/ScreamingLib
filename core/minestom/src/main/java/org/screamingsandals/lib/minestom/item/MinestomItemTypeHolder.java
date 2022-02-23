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

package org.screamingsandals.lib.minestom.item;

import net.minestom.server.item.Material;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;
import java.util.Optional;

public class MinestomItemTypeHolder extends BasicWrapper<Material> implements ItemTypeHolder {
    private short forcedDurability;

    protected MinestomItemTypeHolder(Material wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public short forcedDurability() {
        return forcedDurability;
    }

    @Override
    public int maxStackSize() {
        return wrappedObject.maxStackSize();
    }

    @Override
    public ItemTypeHolder withForcedDurability(short durability) {
        final var holder = new MinestomItemTypeHolder(wrappedObject);
        holder.forcedDurability = durability;
        return holder;
    }

    @Override
    public Optional<BlockTypeHolder> block() {
        if (!wrappedObject.isBlock()) {
            return Optional.empty();
        }
        // TODO: add once MinestomBlockTypeHolder exists
        return Optional.empty();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Material || object instanceof ItemTypeHolder) {
            return equals(object);
        }
        return equals(ItemTypeHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
