package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SBlockFertilizeEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitBlockFertilizeEvent implements SBlockFertilizeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final BlockFertilizeEvent event;

    // Internal cache
    private PlayerWrapper player;
    private boolean playerConverted;
    private BlockHolder block;
    private Collection<BlockStateHolder> changedBlockStates;

    @Override
    public PlayerWrapper getPlayer() {
        if (!playerConverted) {
            if (event.getPlayer() != null) {
                player = new BukkitEntityPlayer(event.getPlayer());
            }
            playerConverted = true;
        }
        return player;
    }

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public Collection<BlockStateHolder> getChangedBlockStates() {
        if (changedBlockStates == null) {
            changedBlockStates = new CollectionLinkedToCollection<>(event.getBlocks(), o -> o.as(BlockState.class), o -> BlockStateMapper.wrapBlockState(o).orElseThrow());
        }
        return changedBlockStates;
    }
}
