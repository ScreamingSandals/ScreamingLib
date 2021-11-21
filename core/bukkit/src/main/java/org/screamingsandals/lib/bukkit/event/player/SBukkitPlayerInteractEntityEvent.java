package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.player.SPlayerInteractEntityEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;

@Data
public class SBukkitPlayerInteractEntityEvent implements SPlayerInteractEntityEvent, BukkitCancellable {
    private final PlayerInteractEntityEvent event;

    // Internal cache
    private PlayerWrapper player;
    private EntityBasic clickedEntity;
    private EquipmentSlotHolder hand;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public EntityBasic getClickedEntity() {
        if (clickedEntity == null) {
            clickedEntity = EntityMapper.wrapEntity(event.getRightClicked()).orElseThrow();
        }
        return clickedEntity;
    }

    @Override
    public EquipmentSlotHolder getHand() {
        if (hand == null) {
            hand = EquipmentSlotHolder.of(event.getHand());
        }
        return hand;
    }
}
