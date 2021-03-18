package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerBucketFillEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBucketFillEventListener extends AbstractBukkitEventHandlerFactory<PlayerBucketFillEvent, SPlayerBucketFillEvent> {

    public PlayerBucketFillEventListener(Plugin plugin) {
        super(PlayerBucketFillEvent.class, SPlayerBucketFillEvent.class, plugin);
    }

    @Override
    protected SPlayerBucketFillEvent wrapEvent(PlayerBucketFillEvent event, EventPriority priority) {
            return new SPlayerBucketFillEvent(
                    PlayerMapper.wrapPlayer(event.getPlayer()),
                    BlockMapper.wrapBlock(event.getBlock()),
                    BlockMapper.wrapBlock(event.getBlockClicked()),
                    BlockFace.valueOf(event.getBlockFace().name().toUpperCase()),
                    MaterialMapping.resolve(event.getBucket()).orElseThrow(),
                    ItemFactory.build(event.getItemStack()).orElse(null)
                    );
    }
}
