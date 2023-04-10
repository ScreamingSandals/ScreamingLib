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

package org.screamingsandals.lib.bukkit.packet;

import io.netty.buffer.ByteBuf;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.BlockType;
import org.screamingsandals.lib.bukkit.item.BukkitItemType1_8;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.nms.accessors.BlockAccessor;
import org.screamingsandals.lib.nms.accessors.FriendlyByteBufAccessor;
import org.screamingsandals.lib.nms.accessors.ItemStackAccessor;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.vanilla.packet.VanillaPacketWriter;

public class CraftBukkitPacketWriter extends VanillaPacketWriter {
    public CraftBukkitPacketWriter(@NotNull ByteBuf buffer) {
        super(buffer);
    }

    @Override
    public int getEquipmentSlotId(@NotNull EquipmentSlot equipmentSlotHolder) {
        return equipmentSlotHolder.as(org.bukkit.inventory.EquipmentSlot.class).ordinal();
    }

    @Override
    protected @NotNull Object materialHolderToItem(@NotNull ItemType material) {
        return Reflect.getMethod(ClassStorage.CB.CraftMagicNumbers, "getItem", Material.class).invokeStatic(material.as(Material.class));
    }

    @Override
    protected @NotNull Object blockDataToBlockState(@NotNull BlockType blockData) {
        if (Reflect.has("org.bukkit.block.data.BlockData")) {
            return Reflect.fastInvoke(blockData.as(BlockData.class), "getState");
        } else {
            var materialData = blockData.as(MaterialData.class);
            return Reflect.getMethod(ClassStorage.CB.CraftMagicNumbers, "getBlock", Material.class)
                    .invokeStaticResulted(materialData.getItemType())
                    .fastInvoke(BlockAccessor.getMethodFromLegacyData1(), (int) materialData.getData());
        }
    }

    @Override
    public void writeNBTFromItem(@NotNull ItemStack item) {
        final var nmsStack = Reflect.fastInvoke(ClassStorage.stackAsNMS(item.as(org.bukkit.inventory.ItemStack.class)), ItemStackAccessor.getMethodCopy1());

        // create temporary friendly ByteBuf instance that will write the NBT for us.
        final var friendlyByteBuf = Reflect.constructor(FriendlyByteBufAccessor.getType(), ByteBuf.class).construct(getBuffer());

        final var nbtTag = Reflect.fastInvoke(nmsStack, ItemStackAccessor.getMethodGetTag1());
        Reflect.fastInvoke(friendlyByteBuf, FriendlyByteBufAccessor.getMethodWriteNbt1(), nbtTag);
    }

    @Override
    protected short getForcedDurability(@NotNull ItemType material) {
        if (material instanceof BukkitItemType1_8) {
            return ((BukkitItemType1_8) material).forcedDurability();
        }
        return 0;
    }
}
