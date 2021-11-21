package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityExperience;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityExperience;
import org.screamingsandals.lib.event.player.SPlayerItemMendEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerWrapper;

@Data
public class SBukkitPlayerItemMendEvent implements SPlayerItemMendEvent, BukkitCancellable {
    private final PlayerItemMendEvent event;

    // Internal cache
    private PlayerWrapper player;
    private Item item;
    private EntityExperience experienceOrb;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public Item getItem() {
        if (item == null) {
            item = ItemFactory.build(event.getItem()).orElseThrow();
        }
        return item;
    }

    @Override
    public EntityExperience getExperienceOrb() {
        if (experienceOrb == null) {
            experienceOrb = new BukkitEntityExperience(event.getExperienceOrb());
        }
        return experienceOrb;
    }

    @Override
    public int getRepairAmount() {
        return event.getRepairAmount();
    }

    @Override
    public void setRepairAmount(int repairAmount) {
        event.setRepairAmount(repairAmount);
    }
}
