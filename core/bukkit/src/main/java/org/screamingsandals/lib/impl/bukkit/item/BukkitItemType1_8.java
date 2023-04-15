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

package org.screamingsandals.lib.impl.bukkit.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlock1_8;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.impl.item.ItemTypeRegistry;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class BukkitItemType1_8 extends BasicWrapper<Pair<Material, Short>> implements ItemType {

    public BukkitItemType1_8(@NotNull Material material) {
        this(Pair.of(material, (short) 0));
    }

    public BukkitItemType1_8(@NotNull Material material, short forcedDurability) {
        this(Pair.of(material, forcedDurability));
    }

    public BukkitItemType1_8(@NotNull Pair<@NotNull Material, @NotNull Short> wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.first().name();
    }

    public short forcedDurability() {
        return wrappedObject.second();
    }

    @Override
    public int maxStackSize() {
        return wrappedObject.first().getMaxStackSize();
    }

    @Override
    public @Nullable Block block() {
        if (!wrappedObject.first().isBlock()) {
            String name = null;
            switch (wrappedObject.first().name()) {
                // these are basically items directly representing blocks
                case "BREWING_STAND_ITEM":
                    name = "BREWING_STAND";
                    break;
                case "CAULDRON_ITEM":
                    name = "CAULDRON";
                    break;
                case "FLOWER_POT_ITEM":
                    name = "FLOWER_POT";
                    break;
                case "SKULL_ITEM":
                    name = "SKULL"; // TODO: Tile entity and specific type representation
                    break;
                case "WOOD_DOOR":
                    name = "WOODEN_DOOR";
                    break;
                case "IRON_DOOR":
                    name = "IRON_DOOR_BLOCK";
                    break;
                case "SPRUCE_DOOR_ITEM":
                    name = "SPRUCE_DOOR";
                    break;
                case "BIRCH_DOOR_ITEM":
                    name = "BIRCH_DOOR";
                    break;
                case "JUNGLE_DOOR_ITEM":
                    name = "JUNGLE_DOOR";
                    break;
                case "ACACIA_DOOR_ITEM":
                    name = "ACACIA_DOOR";
                    break;
                case "DARK_OAK_DOOR_ITEM":
                    name = "DARK_OAK_DOOR";
                    break;
                case "BED":
                    name = "BED_BLOCK"; // TODO: Tile entity and bed color representation
                    break;
                case "SUGAR_CANE":
                    name = "SUGAR_CANE_BLOCK";
                    break;
                case "CAKE":
                    name = "CAKE_BLOCK";
                    break;
                case "DIODE":
                    name = "DIODE_BLOCK_OFF";
                    break;
                case "REDSTONE_COMPARATOR":
                    name = "REDSTONE_COMPARATOR_OFF";
                    break;
            }
            try {
                return new BukkitBlock1_8(Material.valueOf(name));
            } catch (IllegalArgumentException ignored) {
            }
            return null;
        }
        var data = wrappedObject.second().byteValue();
        switch (wrappedObject.first().name()) { // item -> block data value conversion
            case "ANVIL":
                data = (byte) ((data & 0x3) << 2); // properly convert anvil
                break;
            case "TORCH":
            case "REDSTONE_TORCH_ON":
                data = 5; // default value for standing torch
                break;
            case "CHEST":
            case "FURNACE":
            case "LADDER":
            case "ENDER_CHEST":
            case "TRAPPED_CHEST":
                data = 2; // default value for containers
                break;
        }
        return new BukkitBlock1_8(wrappedObject.first(), data);
    }

    @Override
    public boolean hasTag(@NotNull Object tag) {
        ResourceLocation key;
        if (tag instanceof ResourceLocation) {
            key = (ResourceLocation) tag;
        } else {
            key = ResourceLocation.of(tag.toString());
        }
        if (!"minecraft".equals(key.namespace())) {
            return false;
        }
        return BukkitItemTypeRegistry1_8.hasTagInBackPorts(wrappedObject.first(), key.path());
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof Material && wrappedObject.second() == 0) {
            return wrappedObject.first() == object;
        }
        if (object instanceof ItemType) {
            return equals(object);
        }
        if (object instanceof String) {
            var str = (String) object;
            if (str.startsWith("#")) {
                // seems like a tag
                return hasTag(str.substring(1));
            }
        }
        return equals(ItemType.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == Material.class) { // the wrapped type is Pair, so we will help the BasicWrapper a little with unwrapping Material
            return (T) wrappedObject.first();
        } else if (type == ItemStack.class) {
            return (T) new ItemStack(wrappedObject.first(), 1, wrappedObject.second());
        }
        return super.as(type);
    }

    @Override
    public @NotNull ResourceLocation location() {
        var registry = ItemTypeRegistry.getInstance();
        if (registry instanceof BukkitItemTypeRegistry1_8) {
            var location = ((BukkitItemTypeRegistry1_8) registry).getResourceLocations().get(this);
            if (location != null) {
                return location;
            }
        }
        return ResourceLocation.of("minecraft:legacy/" + wrappedObject.first().name() + "/" + wrappedObject.second());
    }
}
