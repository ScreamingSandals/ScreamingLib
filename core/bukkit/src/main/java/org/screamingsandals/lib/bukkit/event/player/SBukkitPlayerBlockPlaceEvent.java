package org.screamingsandals.lib.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.SPlayerBlockPlaceEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableCollectionLinkedToCollection;

import java.util.Collection;
import java.util.List;


@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerBlockPlaceEvent implements SPlayerBlockPlaceEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final BlockPlaceEvent event;

    // Internal cache;
    private PlayerWrapper player;
    private Collection<BlockStateHolder> replacedBlockStates;
    private PlayerWrapper.Hand playerHand;
    private BlockHolder block;
    private BlockStateHolder replacedBlockState;
    private Item itemInHand;

    @Override
    public Collection<BlockStateHolder> getReplacedBlockStates() {
        if (replacedBlockStates == null) {
            if (event instanceof BlockMultiPlaceEvent) {
                replacedBlockStates = new ImmutableCollectionLinkedToCollection<>(
                        ((BlockMultiPlaceEvent) event).getReplacedBlockStates(),
                        blockStateHolder -> blockStateHolder.as(BlockState.class),
                        blockState -> BlockStateMapper.wrapBlockState(blockState).orElseThrow()
                );
            } else {
                replacedBlockStates = List.of(getReplacedBlockState());
            }
        }
        return replacedBlockStates;
    }

    @Override
    public PlayerWrapper.Hand getPlayerHand() {
        if (playerHand == null) {
            playerHand = PlayerMapper.wrapHand(event.getHand());
        }
        return playerHand;
    }

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public BlockStateHolder getReplacedBlockState() {
        if (replacedBlockState == null) {
            replacedBlockState = BlockStateMapper.wrapBlockState(event.getBlockReplacedState()).orElseThrow();
        }
        return replacedBlockState;
    }

    @Override
    public Item getItemInHand() {
        if (itemInHand == null) {
            itemInHand = new BukkitItem(event.getItemInHand());
        }
        return itemInHand;
    }

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }
}
