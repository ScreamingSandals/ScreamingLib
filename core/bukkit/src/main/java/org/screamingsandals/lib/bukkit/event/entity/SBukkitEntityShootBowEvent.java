package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityShootBowEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;

@Data
public class SBukkitEntityShootBowEvent implements SEntityShootBowEvent, BukkitCancellable {
    private final EntityShootBowEvent event;

    // Internal cache
    private EntityBasic entity;
    private Item bow;
    private boolean bowCached;
    private Item consumable;
    private boolean consumableCached;
    private EquipmentSlotHolder hand;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    @Nullable
    public Item getBow() {
        if (!bowCached) {
            if (event.getBow() != null) {
                bow = ItemFactory.build(event.getBow()).orElseThrow();
            }
            bowCached = true;
        }
        return bow;
    }

    @Override
    @Nullable
    public Item getConsumable() {
        if (!consumableCached) {
            if (event.getBow() != null) {
                consumable = ItemFactory.build(event.getConsumable()).orElseThrow();
            }
            consumableCached = true;
        }
        return consumable;
    }

    @Override
    public EntityBasic getProjectile() {
        return EntityMapper.wrapEntity(event.getProjectile()).orElseThrow(); // Mutable in Bukkit
    }

    @Override
    public EquipmentSlotHolder getHand() {
        if (hand == null) {
            hand = EquipmentSlotHolder.of(event.getHand());
        }
        return hand;
    }

    @Override
    public float getForce() {
        return event.getForce();
    }

    @Override
    public boolean isConsumeItem() {
        return event.shouldConsumeItem();
    }

    @Override
    public void setConsumeItem(boolean consumeItem) {
        event.setConsumeItem(consumeItem);
    }
}
