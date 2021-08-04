package org.screamingsandals.lib.bukkit.packet;

import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.material.MaterialData;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.nms.accessors.BlockAccessor;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.vanilla.packet.VanillaPacketWriter;
import org.screamingsandals.lib.world.BlockDataHolder;

public class CraftBukkitPacketWriter extends VanillaPacketWriter {
    public CraftBukkitPacketWriter(ByteBuf buffer) {
        super(buffer);
    }

    @Override
    public int getEquipmentSlotId(EquipmentSlotHolder equipmentSlotHolder) {
        return equipmentSlotHolder.as(EquipmentSlot.class).ordinal();
    }

    @Override
    protected Object materialHolderToItem(MaterialHolder material) {
        return Reflect.getMethod(ClassStorage.CB.CraftMagicNumbers, "getItem", Material.class).invokeStatic(material.as(Material.class));
    }

    @Override
    protected Object blockDataToBlockState(BlockDataHolder blockData) {
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
    protected Object getMinecraftServerInstance() {
        return Reflect.fastInvoke(Bukkit.getServer(), "getServer");
    }
}
