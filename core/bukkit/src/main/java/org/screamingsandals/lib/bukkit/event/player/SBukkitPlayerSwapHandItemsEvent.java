package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.SPlayerSwapHandItemsEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerSwapHandItemsEvent implements SPlayerSwapHandItemsEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerSwapHandItemsEvent event;

    // Internal cache
    private PlayerWrapper player;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    @Nullable
    public Item getMainHandItem() {
        return event.getMainHandItem() == null ? null : new BukkitItem(event.getMainHandItem());
    }

    @Override
    public void setMainHandItem(@Nullable Item mainHandItem) {
        event.setMainHandItem(mainHandItem == null ? null : mainHandItem.as(ItemStack.class));
    }

    @Override
    @Nullable
    public Item getOffHandItem() {
        return event.getOffHandItem() == null ? null : new BukkitItem(event.getOffHandItem());
    }

    @Override
    public void setOffHandItem(@Nullable Item offHandItem) {
        event.setOffHandItem(offHandItem == null ? null : offHandItem.as(ItemStack.class));
    }
}
