package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.player.SPlayerShearEntityEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;

@Data
public class SBukkitPlayerShearEntityEvent implements SPlayerShearEntityEvent, BukkitCancellable {
    private final PlayerShearEntityEvent event;

    // Internal cache
    private PlayerWrapper player;
    private EntityBasic what;
    private Item item;
    private EquipmentSlotHolder hand;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public EntityBasic getWhat() {
        if (what == null) {
            what = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return what;
    }

    @Override
    public Item getItem() {
        if (item == null) {
            item = ItemFactory.build(event.getItem()).orElseThrow();
        }
        return item;
    }

    @Override
    public EquipmentSlotHolder getHand() {
        if (hand == null) {
            hand = EquipmentSlotHolder.of(event.getHand());
        }
        return hand;
    }
}
