package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.block.SBlockDispenseEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.utils.math.Vector3D;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitBlockDispenseEvent implements SBlockDispenseEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final BlockDispenseEvent event;

    // Internal cache
    private BlockHolder block;
    private EntityLiving receiver;
    private boolean receiverCached;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public Item getItem() {
        return new BukkitItem(event.getItem());
    }

    @Override
    public void setItem(Item item) {
        event.setItem(item.as(ItemStack.class));
    }

    @Override
    public Vector3D getVelocity() {
        return new Vector3D(event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ());
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        event.setVelocity(new Vector(velocity.getX(), velocity.getY(), velocity.getZ()));
    }

    @Override
    public @Nullable EntityLiving getReceiver() {
        if (!receiverCached) {
            if (event instanceof BlockDispenseArmorEvent) {
                receiver = EntityMapper.<EntityLiving>wrapEntity(((BlockDispenseArmorEvent) event).getTargetEntity()).orElseThrow();
            }
            receiverCached = true;
        }
        return receiver;
    }
}
