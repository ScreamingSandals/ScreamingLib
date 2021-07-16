package org.screamingsandals.lib.bukkit.packet;

import com.mojang.datafixers.util.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundSetEquipmentPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundSetEquipmentPacket;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.List;

public class BukkitSClientboundSetEquipmentPacket extends BukkitSPacket implements SClientboundSetEquipmentPacket {

    public BukkitSClientboundSetEquipmentPacket() {
        super(ClientboundSetEquipmentPacketAccessor.getType());
    }

    @Override
    public SClientboundSetEquipmentPacket setEntityId(int entityId) {
        packet.setField(ClientboundSetEquipmentPacketAccessor.getFieldEntity(), entityId);
        return this;
    }

    @Override
    public SClientboundSetEquipmentPacket setItemAndSlot(Item item, Slot slot) {
        if (slot == null) {
            throw new UnsupportedOperationException("Slot cannot be null!");
        }
        if (item == null) {
            item = ItemFactory.build(Material.AIR).orElseThrow();
        }
        if (isOldPacket()) {
            packet.setField("c,field_149393_c", ClassStorage.stackAsNMS(item.as(ItemStack.class)));
            packet.setField("b,field_149392_b", getSlot(slot));
        } else {
            final var data = List.of(Pair.of(getSlot(slot), ClassStorage.stackAsNMS(item.as(ItemStack.class))));
            packet.setField(ClientboundSetEquipmentPacketAccessor.getFieldSlots(), data);
        }
        return this;
    }

    protected Object getSlot(Slot slot) {
        if (slot == null) {
            throw new UnsupportedOperationException("Slot cannot be null!");
        }
        EquipmentSlot bukkitSlot = EquipmentSlot.valueOf(slot.name().toUpperCase());
        return Reflect.getMethod(ClassStorage.NMS.CraftEquipmentSlot, "getNMS", EquipmentSlot.class).invokeStatic(bukkitSlot);
    }

    protected boolean isOldPacket() {
        return Reflect.constructor(ClassStorage.NMS.PacketPlayOutEntityEquipment, int.class, List.class).isEmpty();
    }
}
