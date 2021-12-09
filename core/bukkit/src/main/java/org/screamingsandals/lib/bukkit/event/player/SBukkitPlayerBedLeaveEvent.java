package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerBedLeaveEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerBedLeaveEvent implements SPlayerBedLeaveEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerBedLeaveEvent event;

    // Internal cache
    private PlayerWrapper player;
    private BlockHolder bed;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public BlockHolder getBed() {
        if (bed == null) {
            bed = BlockMapper.wrapBlock(event.getBed());
        }
        return bed;
    }

    @Override
    public boolean isBedSpawn() {
        return event.shouldSetSpawnLocation();
    }

    @Override
    public void setBedSpawn(boolean bedSpawn) {
        event.setSpawnLocation(bedSpawn);
    }
}
