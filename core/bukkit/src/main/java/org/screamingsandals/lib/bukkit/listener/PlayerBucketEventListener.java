package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerBucketEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBucketEventListener extends AbstractBukkitEventHandlerFactory<PlayerBucketEvent, SPlayerBucketEvent> {

    @SuppressWarnings("unchecked")
    public PlayerBucketEventListener(Plugin plugin, Class<? extends PlayerBucketEvent> theClass) {
        super((Class<PlayerBucketEvent>) theClass, SPlayerBucketEvent.class, plugin);
    }

    @Override
    protected SPlayerBucketEvent wrapEvent(PlayerBucketEvent event, EventPriority priority) {
        return new SPlayerBucketEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlockClicked())),
                ImmutableObjectLink.of(() -> BlockFace.valueOf(event.getBlockFace().name().toUpperCase())),
                ImmutableObjectLink.of(() -> MaterialHolder.of(event.getBucket())),
                ObjectLink.of(
                        () -> ItemFactory.build(event.getItemStack()).orElse(null),
                        item -> event.setItemStack(item.as(ItemStack.class))
                ),
                ImmutableObjectLink.of(() -> event instanceof PlayerBucketFillEvent ? SPlayerBucketEvent.Action.FILL : SPlayerBucketEvent.Action.EMPTY)
        );
    }
}
